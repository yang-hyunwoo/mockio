package com.mockio.gateway.config;

import com.mockio.gateway.handler.GatewayAccessDeniedHandler;
import com.mockio.gateway.handler.GatewayAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GatewayAuthenticationEntryPoint gatewayAuthenticationEntryPoint;
    private final GatewayAccessDeniedHandler gatewayAccessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeExchange(ex -> ex
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(
                                "/health",
                                "/actuator/health/**",
                                "/api/auth/v1/.well-known/jwks.json",
                                "/api/users/v1/public/**",
                                "/api/user-interview/v1/public/**",
                                "/api/notification/v1/public/**",
                                "/api/chat/v1/public/**",
                                "/api/auth/v1/login",
                                "/api/auth/v1/logout",
                                "/api/ai/v1/public/**",
                                "/api/auth/v1/public/**",
                                "/api/feedback/v1/public/**",
                                "/api/noti/v1/public/**",
                                "/api/inquiry/v1/public/**",
                                "/api/file/v1/public/**",
                                "/api/faq/v1/public/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(gatewayAuthenticationEntryPoint)
                        .accessDeniedHandler(gatewayAccessDeniedHandler)
                )
                .build();
    }


}