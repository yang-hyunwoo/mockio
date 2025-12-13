package com.mockio.auth_service.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

//    private static final String KEYCLOAK_IP = "172.19.0.3";
private static final String KEYCLOAK_IP = "127.0.0.1";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/naver/userinfo").access(this::keycloakIpOnly)
                        .anyRequest().denyAll()
                ).build();
    }

    private AuthorizationDecision keycloakIpOnly(
            Supplier<Authentication> authentication,
            RequestAuthorizationContext context
    ) {
        HttpServletRequest request = context.getRequest();
        String remoteAddr = request.getRemoteAddr();

        boolean allowed = KEYCLOAK_IP.equals(remoteAddr);
        return new AuthorizationDecision(allowed);
    }
}