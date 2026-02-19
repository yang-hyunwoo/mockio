package com.mockio.gateway.config;

import com.mockio.gateway.handler.GatewayAccessDeniedHandler;
import com.mockio.gateway.handler.GatewayAuthenticationEntryPoint;
import com.mockio.gateway.properties.JwtConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtConfigProperties jwtConfigProperties;
    private final GatewayAuthenticationEntryPoint gatewayAuthenticationEntryPoint;
    private final GatewayAccessDeniedHandler gatewayAccessDeniedHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(
                                "/health",
                                "/actuator/health",
                                "/actuator/health/**"
                        ).permitAll()
                        .pathMatchers("/api/users/v1/public/**",
                                "/api/user-interview/v1/public/**",
                                "/api/notification/v1/public/**",
                                "/api/chat/v1/public/**",
                                "/api/ai/v1/public/**",
                                "/api/auth/v1/public/**",
                                "/api/feedback/v1/public/**",
                                "/api/noti/v1/public/**",
                                "/api/inquiry/v1/public/**",
                                "/api/faq/v1/public/**").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(gatewayAuthenticationEntryPoint) //인증 실패
                        .accessDeniedHandler(gatewayAccessDeniedHandler))          //인가 실패
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Keycloak의 issuer, jwk-set-uri 사용
        return JwtDecoders.fromIssuerLocation(jwtConfigProperties.getIssuerUri());
    }

}
