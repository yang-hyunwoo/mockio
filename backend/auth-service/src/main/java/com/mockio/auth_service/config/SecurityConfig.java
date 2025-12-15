package com.mockio.auth_service.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String KEYCLOAK_IP = "127.0.0.1";

    /**
     * 1) Keycloak 전용 프록시 엔드포인트: IP allowlist
     */
    @Bean
    @Order(1)
    public SecurityFilterChain keycloakProxyChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/auth/v1/naver/userinfo")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().access(this::keycloakIpOnly)
                )
                .build();
    }

    /**
     * 2) 클라이언트가 직접 호출하는 인증 허브 엔드포인트:
     * - refresh / logout 등은 쿠키 기반 검증을 컨트롤러/서비스에서 수행하므로 permitAll
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authHubClientChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(
                        "/api/auth/v1/refresh",
                        "/api/auth/v1/logout",
                        "/api/auth/v1/logout/all"
                )
                .csrf(csrf -> csrf.disable()) // 쿠키를 쓰더라도 refresh/logout는 보통 CSRF 미사용(대신 SameSite/Origin 정책으로 방어)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }

    /**
     * 3) 나머지 전부 차단
     */
    @Bean
    @Order(3)
    public SecurityFilterChain denyAllChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().denyAll()
                )
                .build();
    }

    private AuthorizationDecision keycloakIpOnly(
            Supplier<Authentication> authentication,
            RequestAuthorizationContext context
    ) {
        HttpServletRequest request = context.getRequest();
        String remoteAddr = request.getRemoteAddr();
        return new AuthorizationDecision(KEYCLOAK_IP.equals(remoteAddr));
    }
}