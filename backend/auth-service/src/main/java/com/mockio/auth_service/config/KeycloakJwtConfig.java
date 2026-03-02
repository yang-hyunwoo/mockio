package com.mockio.auth_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
public class KeycloakJwtConfig {

    @Bean
    JwtDecoder keycloakJwtDecoder(@Value("${keycloak.issuer}") String issuer) {
        return JwtDecoders.fromIssuerLocation(issuer);
    }
}
