package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.dto.TokenRefreshRequestDto;
import kr.hs.sdh.digitechnetwork.dto.TokenRefreshResponseDto;
import kr.hs.sdh.digitechnetwork.dto.UserVerificationRequestDto;
import kr.hs.sdh.digitechnetwork.dto.UserVerificationResponseDto;
import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.entity.Teacher;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import kr.hs.sdh.digitechnetwork.exception.ResourceNotFoundException;
import kr.hs.sdh.digitechnetwork.repository.StudentRepository;
import kr.hs.sdh.digitechnetwork.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * 사용자 검증 서비스 구현체
 * JWT 토큰을 통한 사용자 검증 및 토큰 갱신을 담당
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserVerificationServiceImpl implements UserVerificationService {

    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public UserVerificationResponseDto verifyUser(UserVerificationRequestDto requestDto) {
        log.info("사용자 검증 요청: 토큰 검증 시작");
        
        String token = requestDto.getAccessToken();
        
        // 토큰 유효성 검증
        if (!jwtService.validateToken(token)) {
            log.warn("유효하지 않은 토큰: {}", token);
            return createInvalidResponse();
        }
        
        // 토큰 만료 확인
        if (jwtService.isTokenExpired(token)) {
            log.warn("만료된 토큰: {}", token);
            return createInvalidResponse();
        }
        
        try {
            // 토큰에서 사용자 정보 추출
            UserType userType = jwtService.getUserTypeFromToken(token);
            Long userId = jwtService.getUserIdFromToken(token);
            String email = jwtService.getEmailFromToken(token);
            Date expiration = jwtService.getExpirationFromToken(token);
            
            // 사용자 정보 조회
            Optional<?> userOptional = findUserByTypeAndId(userType, userId);
            
            if (userOptional.isEmpty()) {
                log.warn("토큰의 사용자 정보를 찾을 수 없음: userType={}, userId={}", userType, userId);
                return createInvalidResponse();
            }
            
            // 사용자 정보로 응답 생성
            return createVerificationResponse(userOptional.get(), userType, expiration);
            
        } catch (Exception e) {
            log.error("사용자 검증 중 오류 발생: {}", e.getMessage(), e);
            return createInvalidResponse();
        }
    }

    @Override
    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto requestDto) {
        log.info("토큰 갱신 요청");
        
        try {
            String newAccessToken = jwtService.refreshAccessToken(requestDto.getRefreshToken());
            Date expiration = jwtService.getExpirationFromToken(newAccessToken);
            
            return TokenRefreshResponseDto.builder()
                    .accessToken(newAccessToken)
                    .tokenType("Bearer")
                    .expiresIn(expiration.getTime() / 1000) // 초 단위로 변환
                    .build();
                    
        } catch (Exception e) {
            log.error("토큰 갱신 중 오류 발생: {}", e.getMessage(), e);
            throw new IllegalArgumentException("토큰 갱신에 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token) && !jwtService.isTokenExpired(token);
    }

    /**
     * 사용자 타입과 ID로 사용자 조회
     * @param userType 사용자 타입
     * @param userId 사용자 ID
     * @return 사용자 정보 (Optional)
     */
    private Optional<?> findUserByTypeAndId(UserType userType, Long userId) {
        return switch (userType) {
            case STUDENT -> studentRepository.findById(userId);
            case TEACHER -> teacherRepository.findById(userId);
            default -> Optional.empty();
        };
    }

    /**
     * 검증 실패 응답 생성
     * @return 검증 실패 응답
     */
    private UserVerificationResponseDto createInvalidResponse() {
        return UserVerificationResponseDto.builder()
                .isValid(false)
                .build();
    }

    /**
     * 검증 성공 응답 생성
     * @param user 사용자 엔티티
     * @param userType 사용자 타입
     * @param expiration 토큰 만료 시간
     * @return 검증 성공 응답
     */
    private UserVerificationResponseDto createVerificationResponse(Object user, UserType userType, Date expiration) {
        UserVerificationResponseDto.UserVerificationResponseDtoBuilder builder = UserVerificationResponseDto.builder()
                .userType(userType)
                .isValid(true)
                .tokenExpiration(LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault()));
        
        if (user instanceof Student student) {
            builder.userId(student.getId())
                    .email(student.getEmail())
                    .name(student.getName())
                    .isEnabled(student.getIsEnabled())
                    .bio(student.getBio())
                    .grade(student.getGrade())
                    .classroom(student.getClassroom())
                    .studentNumber(student.getStudentNumber());
        } else if (user instanceof Teacher teacher) {
            builder.userId(teacher.getId())
                    .email(teacher.getEmail())
                    .name(teacher.getName())
                    .isEnabled(teacher.getIsEnabled())
                    .bio(teacher.getBio());
        }
        
        return builder.build();
    }
}
