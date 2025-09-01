package kr.hs.sdh.digitechnetwork.dto;

import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 검증 응답 DTO
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationResponseDto {
    
    private Long userId;
    private String email;
    private String name;
    private UserType userType;
    private Boolean isEnabled;
    private String bio;
    private LocalDateTime tokenExpiration;
    private Boolean isValid;
    
    // Student 전용 필드
    private Integer grade;
    private Integer classroom;
    private Integer studentNumber;
    
    // Teacher 전용 필드
    private String subject;
    private String position;
}
