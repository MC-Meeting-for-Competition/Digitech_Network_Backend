package kr.hs.sdh.digitechnetwork.controller;

import kr.hs.sdh.digitechnetwork.dto.AuthResponseDto;
import kr.hs.sdh.digitechnetwork.dto.GoogleOAuthRequestDto;
import kr.hs.sdh.digitechnetwork.service.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final GoogleOAuthService googleOAuthService;
    
    /**
     * Google OAuth 로그인 URL 생성
     */
    @GetMapping("/google/login")
    public ResponseEntity<Map<String, String>> getGoogleLoginUrl() {
        String authUrl = googleOAuthService.getAuthorizationUrl();
        return ResponseEntity.ok(Map.of("authUrl", authUrl));
    }
    
    /**
     * Google OAuth 콜백 처리
     */
    @PostMapping("/google/callback")
    public ResponseEntity<AuthResponseDto> googleOAuthCallback(@RequestBody GoogleOAuthRequestDto requestDto) {
        log.info("Google OAuth callback received with code: {}", requestDto.getCode());
        
        AuthResponseDto authResponse = googleOAuthService.authenticate(requestDto);
        
        return ResponseEntity.ok(authResponse);
    }
    
    /**
     * Google OAuth 콜백 처리 (GET 방식 - 브라우저 리다이렉트용)
     */
    @GetMapping("/google/callback")
    public ResponseEntity<AuthResponseDto> googleOAuthCallbackGet(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state) {
        
        log.info("Google OAuth callback received with code: {}", code);
        
        GoogleOAuthRequestDto requestDto = GoogleOAuthRequestDto.builder()
                .code(code)
                .state(state)
                .build();
        
        AuthResponseDto authResponse = googleOAuthService.authenticate(requestDto);
        
        return ResponseEntity.ok(authResponse);
    }
}
