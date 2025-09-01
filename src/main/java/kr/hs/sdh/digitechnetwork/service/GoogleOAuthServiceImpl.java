package kr.hs.sdh.digitechnetwork.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hs.sdh.digitechnetwork.auth.GoogleOAuth;
import kr.hs.sdh.digitechnetwork.dto.AuthResponseDto;
import kr.hs.sdh.digitechnetwork.dto.GoogleOAuthRequestDto;
import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.entity.Teacher;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import kr.hs.sdh.digitechnetwork.exception.BusinessException;
import kr.hs.sdh.digitechnetwork.exception.ErrorCode;
import kr.hs.sdh.digitechnetwork.repository.StudentRepository;
import kr.hs.sdh.digitechnetwork.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Google OAuth 서비스 구현체
 * Google OAuth 2.0 인증 플로우를 처리
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    private final GoogleOAuth googleOAuth;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Value("${google.oauth.client-id}")
    private String clientId;

    @Value("${google.oauth.client-secret}")
    private String clientSecret;

    @Value("${google.oauth.redirect-uri}")
    private String redirectUri;

    @Override
    public String getAuthorizationUrl() {
        return googleOAuth.getRedirectUri();
    }

    @Override
    @Transactional
    public AuthResponseDto authenticate(GoogleOAuthRequestDto requestDto) {
        log.info("Google OAuth 인증 시작: code={}", requestDto.getCode());

        try {
            // 1. 액세스 토큰 요청
            Map<String, Object> tokenResponse = getAccessToken(requestDto.getCode());
            String accessToken = (String) tokenResponse.get("access_token");

            // 2. 사용자 정보 요청
            Map<String, Object> userInfo = getUserInfo(accessToken);

            // 3. 사용자 로그인 처리
            AuthResponseDto.UserInfoDto userInfoDto = processUserLogin(userInfo);

            // 4. JWT 토큰 생성
            Map<String, String> tokens = generateJwtTokens(userInfoDto);

            // 5. 응답 생성
            return createAuthResponse(tokens, userInfoDto);

        } catch (Exception e) {
            log.error("Google OAuth 인증 실패: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.OAUTH_AUTHENTICATION_FAILED, "Google OAuth 인증에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 액세스 토큰 요청
     */
    private Map<String, Object> getAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        String body = String.format(
                "client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
                clientId, clientSecret, code, redirectUri
        );
        
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("액세스 토큰 요청 실패");
        }
        
        return response.getBody();
    }

    /**
     * 사용자 정보 요청
     */
    private Map getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
        
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("사용자 정보 요청 실패");
        }
        
        return response.getBody();
    }

    /**
     * 사용자 로그인 처리
     */
    private AuthResponseDto.UserInfoDto processUserLogin(Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        
        log.info("사용자 로그인 처리: email={}, name={}", email, name);

        // 기존 학생 사용자 확인
        var studentOptional = studentRepository.findByEmail(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            log.info("기존 학생 사용자 로그인: {}", email);
            return createUserInfoFromStudent(student);
        }

        // 기존 교사 사용자 확인
        var teacherOptional = teacherRepository.findByEmail(email);
        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            log.info("기존 교사 사용자 로그인: {}", email);
            return createUserInfoFromTeacher(teacher);
        }

        // 새로운 사용자 생성 (기본적으로 학생으로 생성)
        log.info("새로운 사용자 생성: {}", email);
        
        Student newStudent = createNewStudent(userInfo);
        Student savedStudent = studentRepository.save(newStudent);
        return createUserInfoFromStudent(savedStudent);
    }

    /**
     * JWT 토큰 생성
     */
    private Map<String, String> generateJwtTokens(AuthResponseDto.UserInfoDto userInfo) {
        if (userInfo.getGrade() != null) {
            // Student인 경우
            Student student = studentRepository.findById(userInfo.getId())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            return jwtService.generateTokensFromStudent(student);
        } else {
            // Teacher인 경우
            Teacher teacher = teacherRepository.findById(userInfo.getId())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            return jwtService.generateTokensFromTeacher(teacher);
        }
    }

    /**
     * 인증 응답 생성
     */
    private AuthResponseDto createAuthResponse(Map<String, String> tokens, AuthResponseDto.UserInfoDto userInfo) {
        return AuthResponseDto.builder()
                .accessToken(tokens.get("accessToken"))
                .refreshToken(tokens.get("refreshToken"))
                .tokenType(tokens.get("tokenType"))
                .expiresIn(Long.parseLong(tokens.get("expiresIn")))
                .userInfo(userInfo)
                .build();
    }

    /**
     * 새로운 학생 생성
     */
    private Student createNewStudent(Map<String, Object> userInfo) {
        return Student.builder()
                .name((String) userInfo.get("name"))
                .email((String) userInfo.get("email"))
                .phoneNumber("")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .grade(1) // 기본값
                .classroom(1) // 기본값
                .studentNumber(1) // 기본값
                .build();
    }

    /**
     * Student 엔티티로부터 UserInfoDto 생성
     */
    private AuthResponseDto.UserInfoDto createUserInfoFromStudent(Student student) {
        return AuthResponseDto.UserInfoDto.builder()
                .id(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .phoneNumber(student.getPhoneNumber())
                .role(student.getRole())
                .isEnabled(student.getIsEnabled())
                .bio(student.getBio())
                .grade(student.getGrade())
                .classroom(student.getClassroom())
                .studentNumber(student.getStudentNumber())
                .build();
    }

    /**
     * Teacher 엔티티로부터 UserInfoDto 생성
     */
    private AuthResponseDto.UserInfoDto createUserInfoFromTeacher(Teacher teacher) {
        return AuthResponseDto.UserInfoDto.builder()
                .id(teacher.getId())
                .email(teacher.getEmail())
                .name(teacher.getName())
                .phoneNumber(teacher.getPhoneNumber())
                .role(teacher.getRole())
                .isEnabled(teacher.getIsEnabled())
                .bio(teacher.getBio())
                .build();
    }
}
