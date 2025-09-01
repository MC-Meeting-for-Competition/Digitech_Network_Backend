package kr.hs.sdh.digitechnetwork.service;

import kr.hs.sdh.digitechnetwork.dto.AuthResponseDto;
import kr.hs.sdh.digitechnetwork.dto.GoogleOAuthRequestDto;

public interface GoogleOAuthService {
    String getAuthorizationUrl();
    AuthResponseDto authenticate(GoogleOAuthRequestDto requestDto);
}
