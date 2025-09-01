package kr.hs.sdh.digitechnetwork.dto;

import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfoDto userInfo;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDto {
        private Long id;
        private String email;
        private String name;
        private String phoneNumber;
        private UserType role;
        private Boolean isEnabled;
        private String bio;
        // Student specific fields
        private Integer grade;
        private String classroom;
        private Integer studentNumber;
        // Teacher specific fields
        private String subject;
        private String position;
    }
}
