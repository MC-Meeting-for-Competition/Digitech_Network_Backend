package kr.hs.sdh.digitechnetwork.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hs.sdh.digitechnetwork.auth.GoogleOAuth;
import kr.hs.sdh.digitechnetwork.dto.AuthResponseDto;
import kr.hs.sdh.digitechnetwork.dto.GoogleOAuthRequestDto;
import kr.hs.sdh.digitechnetwork.dto.GoogleOAuthResponseDto;
import kr.hs.sdh.digitechnetwork.dto.GoogleUserInfoDto;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthServiceImpl implements GoogleOAuthService {
    
    private final GoogleOAuth googleOAuth;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
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
        try {
            // 1. Authorization code로 access token 요청
            GoogleOAuthResponseDto tokenResponse = getAccessToken(requestDto.getCode());
            
            // 2. Access token으로 사용자 정보 요청
            GoogleUserInfoDto userInfo = getUserInfo(tokenResponse.getAccessToken());

            // 3. 사용자 정보로 로그인 처리
            return processUserLogin(userInfo, tokenResponse);
            
        } catch (Exception e) {
            log.error("Google OAuth authentication failed", e);
            throw new BusinessException(ErrorCode.OAUTH_AUTHENTICATION_FAILED);
        }
    }
    
    private GoogleOAuthResponseDto getAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new BusinessException(ErrorCode.OAUTH_TOKEN_REQUEST_FAILED);
        }
        
        Map responseBody = response.getBody();
        
        return GoogleOAuthResponseDto.builder()
                .accessToken((String) responseBody.get("access_token"))
                .refreshToken((String) responseBody.get("refresh_token"))
                .tokenType((String) responseBody.get("token_type"))
                .expiresIn(((Number) responseBody.get("expires_in")).longValue())
                .scope((String) responseBody.get("scope"))
                .idToken((String) responseBody.get("id_token"))
                .build();
    }
    
    private GoogleUserInfoDto getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl, 
                HttpMethod.GET, 
                request, 
                Map.class
        );
        
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new BusinessException(ErrorCode.OAUTH_USER_INFO_REQUEST_FAILED);
        }
        
        Map userInfoMap = response.getBody();

        if(!userInfoMap.get("hd").equals("sdh.hs.kr")) {
            throw new BusinessException(ErrorCode.OAUTH_USER_INFO_REQUEST_FAILED);
        }
        
        return GoogleUserInfoDto.builder()
                .id((String) userInfoMap.get("id"))
                .email((String) userInfoMap.get("email"))
                .name((String) userInfoMap.get("name"))
                .givenName((String) userInfoMap.get("given_name"))
                .familyName((String) userInfoMap.get("family_name"))
                .picture((String) userInfoMap.get("picture"))
                .locale((String) userInfoMap.get("locale"))
                .verifiedEmail((Boolean) userInfoMap.get("verified_email"))
                .build();
    }
    
    private AuthResponseDto processUserLogin(GoogleUserInfoDto googleUserInfo, GoogleOAuthResponseDto tokenResponse) {
        // 이메일로 기존 사용자 확인
        Optional<Student> existingStudent = studentRepository.findByEmail(googleUserInfo.getEmail());
        Optional<Teacher> existingTeacher = teacherRepository.findByEmail(googleUserInfo.getEmail());
        
        if (existingStudent.isPresent()) {
            return createAuthResponse(existingStudent.get(), tokenResponse);
        } else if (existingTeacher.isPresent()) {
            return createAuthResponse(existingTeacher.get(), tokenResponse);
        } else {
            // 새로운 사용자 생성 (기본적으로 Student로 생성)
            Student newStudent = createNewStudent(googleUserInfo);
            return createAuthResponse(newStudent, tokenResponse);
        }
    }
    
    private Student createNewStudent(GoogleUserInfoDto googleUserInfo) {
        Student student = Student.builder()
                .name(googleUserInfo.getName())
                .email(googleUserInfo.getEmail())
                .hashedPassword("")
                .phoneNumber("")
                .role(UserType.STUDENT)
                .isEnabled(true)
                .bio("서울디지텍고 학생")
                .grade(1)
                .classroom(1)
                .studentNumber(1)
                .build();
        
        return studentRepository.save(student);
    }
    
    private AuthResponseDto createAuthResponse(Student student, GoogleOAuthResponseDto tokenResponse) {
        return AuthResponseDto.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .tokenType(tokenResponse.getTokenType())
                .expiresIn(tokenResponse.getExpiresIn())
                .userInfo(AuthResponseDto.UserInfoDto.builder()
                        .id(student.getId())
                        .email(student.getEmail())
                        .name(student.getName())
                        .phoneNumber(student.getPhoneNumber())
                        .role(student.getRole())
                        .isEnabled(student.getIsEnabled())
                        .bio(student.getBio())
                        .grade(student.getGrade())
                        .classroom(student.getClassroom().toString())
                        .studentNumber(student.getStudentNumber())
                        .build())
                .build();
    }
    
    private AuthResponseDto createAuthResponse(Teacher teacher, GoogleOAuthResponseDto tokenResponse) {
        return AuthResponseDto.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .tokenType(tokenResponse.getTokenType())
                .expiresIn(tokenResponse.getExpiresIn())
                .userInfo(AuthResponseDto.UserInfoDto.builder()
                        .id(teacher.getId())
                        .email(teacher.getEmail())
                        .name(teacher.getName())
                        .phoneNumber(teacher.getPhoneNumber())
                        .role(teacher.getRole())
                        .isEnabled(teacher.getIsEnabled())
                        .bio(teacher.getBio())
                        .build())
                .build();
    }
}
