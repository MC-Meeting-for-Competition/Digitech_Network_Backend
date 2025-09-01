package kr.hs.sdh.digitechnetwork.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GoogleOAuth {
    private final ObjectMapper objectMapper;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${google.oauth.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${google.oauth.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${google.oauth.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    public String getRedirectUri() {
        return getRedirectUri(generateState());
    }

    public String getRedirectUri(String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_REDIRECT_URI);
        params.put("scope", "openid profile email");
        params.put("response_type", "code");
        params.put("access_type", "offline");
        params.put("prompt", "consent");
        params.put("state", state);

        String parameterString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        
        return "https://accounts.google.com/o/oauth2/v2/auth" + "?" + parameterString;
    }

    private String generateState() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
