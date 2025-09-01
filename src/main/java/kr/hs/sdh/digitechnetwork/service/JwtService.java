package kr.hs.sdh.digitechnetwork.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.entity.Teacher;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 토큰 서비스
 * JWT 토큰의 생성, 검증, 갱신을 담당
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnly}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24시간 (밀리초)
    private long expiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7일 (밀리초)
    private long refreshExpiration;

    /**
     * JWT 시크릿 키 생성
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 액세스 토큰 생성
     * @param userType 사용자 타입
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @return 액세스 토큰
     */
    public String generateAccessToken(UserType userType, Long userId, String email) {
        return generateToken(userType, userId, email, expiration);
    }

    /**
     * 리프레시 토큰 생성
     * @param userType 사용자 타입
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @return 리프레시 토큰
     */
    public String generateRefreshToken(UserType userType, Long userId, String email) {
        return generateToken(userType, userId, email, refreshExpiration);
    }

    /**
     * JWT 토큰 생성
     * @param userType 사용자 타입
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @param expiration 만료 시간
     * @return JWT 토큰
     */
    private String generateToken(UserType userType, Long userId, String email, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userType.name());
        claims.put("userId", userId);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 토큰에서 사용자 ID 추출
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 토큰에서 사용자 이메일 추출
     * @param token JWT 토큰
     * @return 사용자 이메일
     */
    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("email", String.class);
    }

    /**
     * 토큰에서 사용자 타입 추출
     * @param token JWT 토큰
     * @return 사용자 타입
     */
    public UserType getUserTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String userTypeStr = claims.get("userType", String.class);
        return UserType.valueOf(userTypeStr);
    }

    /**
     * 토큰에서 만료 시간 추출
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 토큰 유효성 검증
     * @param token JWT 토큰
     * @return 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT 토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰이 만료되었는지 확인
     * @param token JWT 토큰
     * @return 만료 여부
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 토큰에서 Claims 추출
     * @param token JWT 토큰
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰 갱신
     * @param refreshToken 리프레시 토큰
     * @return 새로운 액세스 토큰
     */
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        if (isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("만료된 리프레시 토큰입니다.");
        }

        UserType userType = getUserTypeFromToken(refreshToken);
        Long userId = getUserIdFromToken(refreshToken);
        String email = getEmailFromToken(refreshToken);

        return generateAccessToken(userType, userId, email);
    }

    /**
     * Student 엔티티로부터 토큰 생성
     * @param student Student 엔티티
     * @return 토큰 정보가 포함된 Map
     */
    public Map<String, String> generateTokensFromStudent(Student student) {
        String accessToken = generateAccessToken(student.getRole(), student.getId(), student.getEmail());
        String refreshToken = generateRefreshToken(student.getRole(), student.getId(), student.getEmail());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("tokenType", "Bearer");
        tokens.put("expiresIn", String.valueOf(expiration / 1000)); // 초 단위로 변환

        return tokens;
    }

    /**
     * Teacher 엔티티로부터 토큰 생성
     * @param teacher Teacher 엔티티
     * @return 토큰 정보가 포함된 Map
     */
    public Map<String, String> generateTokensFromTeacher(Teacher teacher) {
        String accessToken = generateAccessToken(teacher.getRole(), teacher.getId(), teacher.getEmail());
        String refreshToken = generateRefreshToken(teacher.getRole(), teacher.getId(), teacher.getEmail());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("tokenType", "Bearer");
        tokens.put("expiresIn", String.valueOf(expiration / 1000)); // 초 단위로 변환

        return tokens;
    }
}
