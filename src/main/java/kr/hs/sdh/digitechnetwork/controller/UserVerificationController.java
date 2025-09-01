package kr.hs.sdh.digitechnetwork.controller;

import kr.hs.sdh.digitechnetwork.dto.TokenRefreshRequestDto;
import kr.hs.sdh.digitechnetwork.dto.TokenRefreshResponseDto;
import kr.hs.sdh.digitechnetwork.dto.UserVerificationRequestDto;
import kr.hs.sdh.digitechnetwork.dto.UserVerificationResponseDto;
import kr.hs.sdh.digitechnetwork.service.UserVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 사용자 검증 REST API 컨트롤러
 * JWT 토큰을 통한 사용자 검증 및 토큰 갱신을 처리
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth/verify")
@RequiredArgsConstructor
@Validated
public class UserVerificationController {

    private final UserVerificationService userVerificationService;

    /**
     * 사용자 검증
     * @param requestDto 검증 요청 정보
     * @return 검증 결과
     */
    @PostMapping("/user")
    public ResponseEntity<UserVerificationResponseDto> verifyUser(@Valid @RequestBody UserVerificationRequestDto requestDto) {
        log.info("사용자 검증 요청");
        
        UserVerificationResponseDto response = userVerificationService.verifyUser(requestDto);
        
        if (response.getIsValid()) {
            log.info("사용자 검증 성공: userId={}, email={}", response.getUserId(), response.getEmail());
            return ResponseEntity.ok(response);
        } else {
            log.warn("사용자 검증 실패");
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * 토큰 갱신
     * @param requestDto 토큰 갱신 요청 정보
     * @return 갱신된 토큰 정보
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto requestDto) {
        log.info("토큰 갱신 요청");
        
        try {
            TokenRefreshResponseDto response = userVerificationService.refreshToken(requestDto);
            log.info("토큰 갱신 성공");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("토큰 갱신 실패: {}", e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 토큰 유효성 검증 (간단한 검증)
     * @param token JWT 토큰
     * @return 유효성 여부
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        log.info("토큰 유효성 검증 요청");
        
        boolean isValid = userVerificationService.validateToken(token);
        
        if (isValid) {
            log.info("토큰 유효성 검증 성공");
            return ResponseEntity.ok(true);
        } else {
            log.warn("토큰 유효성 검증 실패");
            return ResponseEntity.ok(false);
        }
    }
}
