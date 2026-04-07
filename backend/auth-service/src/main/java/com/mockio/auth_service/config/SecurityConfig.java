package com.mockio.auth_service.config;

/**
 * Auth Service 전용 Spring Security 설정 클래스.
 *
 * 엔드포인트 성격에 따라 SecurityFilterChain을 분리하여
 * 서로 다른 보안 정책을 적용한다.
 *
 * 클라이언트가 직접 호출하는 인증 허브 엔드포인트는
 * 컨트롤러 레벨에서 검증하도록 위임한다.
 */

import com.mockio.auth_service.oauth.OAuth2AuthenticationFailureHandler;
import com.mockio.auth_service.oauth.OAuth2AuthenticationSuccessHandler;
import com.mockio.auth_service.oauth.PrincipalOauth2UserService;
import com.mockio.auth_service.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/prometheus",
                                "/actuator/health",
                                "/actuator/health/**",
                                "/api/auth/v1/refresh",
                                "/api/auth/v1/logout",
                                "/api/auth/v1/login",
                                "/api/auth/v1/.well-known/jwks.json",
                                "/api/auth/v1/logout/all",
                                "/api/auth/v1/oauth2/authorization/**",
                                "/api/auth/v1/login/oauth2/code/**",
                                "/v3/api-docs/**",
                                "/api/auth/v1/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/auth/v1/oauth2/authorization"))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/api/auth/v1/login/oauth2/code/**"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(principalOauth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .build();
    }

}
