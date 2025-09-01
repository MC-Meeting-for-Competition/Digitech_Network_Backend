package kr.hs.sdh.digitechnetwork.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hs.sdh.digitechnetwork.entity.Student;
import kr.hs.sdh.digitechnetwork.entity.Teacher;
import kr.hs.sdh.digitechnetwork.enums.UserType;
import kr.hs.sdh.digitechnetwork.repository.StudentRepository;
import kr.hs.sdh.digitechnetwork.repository.TeacherRepository;
import kr.hs.sdh.digitechnetwork.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * JWT 인증 필터
 * 요청 헤더에서 JWT 토큰을 추출하고 검증하여 인증 정보를 설정
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
                UserType userType = jwtService.getUserTypeFromToken(token);
                Long userId = jwtService.getUserIdFromToken(token);
                String email = jwtService.getEmailFromToken(token);
                
                // 사용자 정보 조회
                Optional<?> userOptional = findUserByTypeAndId(userType, userId);
                
                if (userOptional.isPresent()) {
                    // 인증 정보 생성
                    UsernamePasswordAuthenticationToken authentication = createAuthentication(userType, userId, email);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("JWT 인증 성공: userType={}, userId={}, email={}", userType, userId, email);
                } else {
                    log.warn("토큰의 사용자 정보를 찾을 수 없음: userType={}, userId={}", userType, userId);
                }
            }
        } catch (Exception e) {
            log.warn("JWT 토큰 처리 중 오류 발생: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 요청에서 JWT 토큰 추출
     * @param request HTTP 요청
     * @return JWT 토큰 (없으면 null)
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
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
     * 인증 정보 생성
     * @param userType 사용자 타입
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @return 인증 토큰
     */
    private UsernamePasswordAuthenticationToken createAuthentication(UserType userType, Long userId, String email) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userType.name());
        
        return new UsernamePasswordAuthenticationToken(
                email, // principal
                null,  // credentials
                Collections.singletonList(authority)
        );
    }
}
