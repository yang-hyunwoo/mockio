package com.mockio.auth_service.config;

/**
 * Auth Service 전용 Spring Security 설정 클래스.
 *
 * <p>엔드포인트 성격에 따라 SecurityFilterChain을 분리하여
 * 서로 다른 보안 정책을 적용한다.</p>
 *
 * <p>Keycloak 프록시 엔드포인트는 IP allowlist로 보호하고,
 * 클라이언트가 직접 호출하는 인증 허브 엔드포인트는
 * 컨트롤러 레벨에서 검증하도록 위임한다.</p>
 */

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
     * Keycloak 서버만 접근 가능한 프록시 엔드포인트 보안 체인.
     *
     * <p>Keycloak → Auth Service 내부 호출 경로로 사용되며,
     * 요청 IP를 기준으로 allowlist 방식의 접근 제어를 적용한다.</p>
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
     * 클라이언트가 직접 호출하는 인증 허브 엔드포인트 보안 체인.
     *
     * <p>refresh / logout 요청은 쿠키 기반 인증을
     * 컨트롤러 또는 서비스 레이어에서 직접 검증하므로
     * Security 레벨에서는 permitAll로 허용한다.</p>
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authHubClientChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(
                        "/api/auth/v1/refresh",
                        "/api/auth/v1/logout",
                        "/api/auth/v1/logout/all",
                        "/api/auth/v1/public/**"
                )
                .csrf(csrf -> csrf.disable()) // 쿠키를 쓰더라도 refresh/logout는 보통 CSRF 미사용(대신 SameSite/Origin 정책으로 방어)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }

    /**
     * 명시적으로 허용되지 않은 모든 요청을 차단하는 기본 보안 체인.
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

    /**
     * Keycloak 서버 IP만 접근을 허용하는 인가 판단 로직.
     *
     * @return 요청 IP가 allowlist에 포함되면 허용, 아니면 거부
     */
    private AuthorizationDecision keycloakIpOnly(
            Supplier<Authentication> authentication,
            RequestAuthorizationContext context
    ) {
        HttpServletRequest request = context.getRequest();
        String remoteAddr = request.getRemoteAddr();
        return new AuthorizationDecision(KEYCLOAK_IP.equals(remoteAddr));
    }

}
