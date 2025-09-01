package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.dto.TokenRefreshRequestDto;
import kr.hs.sdh.digitechnetwork.dto.TokenRefreshResponseDto;
import kr.hs.sdh.digitechnetwork.dto.UserVerificationRequestDto;
import kr.hs.sdh.digitechnetwork.dto.UserVerificationResponseDto;

/**
 * 사용자 검증 서비스 인터페이스
 * JWT 토큰을 통한 사용자 검증 및 토큰 갱신을 담당
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
public interface UserVerificationService {
    
    /**
     * 사용자 검증
     * @param requestDto 검증 요청 정보
     * @return 검증 결과
     */
    UserVerificationResponseDto verifyUser(UserVerificationRequestDto requestDto);
    
    /**
     * 토큰 갱신
     * @param requestDto 토큰 갱신 요청 정보
     * @return 갱신된 토큰 정보
     */
    TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto requestDto);
    
    /**
     * 토큰 유효성 검증
     * @param token JWT 토큰
     * @return 유효성 여부
     */
    boolean validateToken(String token);
}
