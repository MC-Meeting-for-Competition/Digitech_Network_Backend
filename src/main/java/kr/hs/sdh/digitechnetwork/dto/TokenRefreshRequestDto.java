package kr.hs.sdh.digitechnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 갱신 요청 DTO
 * 
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequestDto {
    
    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;
}
