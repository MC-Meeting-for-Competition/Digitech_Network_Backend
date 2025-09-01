package kr.hs.sdh.digitechnetwork.config;

import kr.hs.sdh.digitechnetwork.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정
 * JWT 기반 인증 및 권한 관리를 담당
 *
 * @since 2025.08.30
 * @author yunjisang sdh230308@sdh.hs.kr
 * @version 1.0.0
 */
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (JWT 사용)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 세션 관리 (STATELESS)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // JWT 필터 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // 요청 권한 설정
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                // 공개 엔드포인트
                .requestMatchers("/").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/auth/verify/**").permitAll()
                
                // 기자재 조회는 인증된 사용자만 접근 가능
                .requestMatchers("/api/v1/equipment").permitAll()
                .requestMatchers("/api/v1/equipment/{id}").permitAll()
                .requestMatchers("/api/v1/equipment/public").permitAll()
                .requestMatchers("/api/v1/equipment/available").permitAll()
                .requestMatchers("/api/v1/equipment/status/{status}").permitAll()
                .requestMatchers("/api/v1/equipment/type/{typeId}").permitAll()
                .requestMatchers("/api/v1/equipment/search").permitAll()
                .requestMatchers("/api/v1/equipment/statistics").permitAll()
                .requestMatchers("/api/v1/equipment/statistics/by-type").permitAll()
                .requestMatchers("/api/v1/equipment/{id}/history").permitAll()
                
                // 기자재 관리 (등록, 수정, 삭제)는 관리자만 접근 가능
                .requestMatchers("/api/v1/equipment").hasRole(UserType.ADMIN.name())
                .requestMatchers("/api/v1/equipment/{id}").hasRole(UserType.ADMIN.name())
                .requestMatchers("/api/v1/equipment/{id}/status").hasRole(UserType.ADMIN.name())
                .requestMatchers("/api/v1/equipment/{id}/publicity").hasRole(UserType.ADMIN.name())
                
                // 관리자 권한 필요
                .requestMatchers("/api/v1/admin/**").hasRole(UserType.ADMIN.name())
                
                // 기타 모든 요청은 인증 필요
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}
