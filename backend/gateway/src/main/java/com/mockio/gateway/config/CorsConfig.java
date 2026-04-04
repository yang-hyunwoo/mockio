package com.mockio.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer{

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final List<String> ALLOWED_ORIGINS = List.of("http://localhost:3000","https://mockio.cloud");
        final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
        final List<String> ALLOWED_HEADERS = List.of("*");
        final List<String> EXPOSED_HEADERS = List.of("Authorization", "refresh-token");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(ALLOWED_ORIGINS);
        config.setAllowedHeaders(ALLOWED_HEADERS);
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setExposedHeaders(EXPOSED_HEADERS);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/v3/api-docs/**")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("https://mockio.cloud")
                .allowedMethods("*");
    }

}

