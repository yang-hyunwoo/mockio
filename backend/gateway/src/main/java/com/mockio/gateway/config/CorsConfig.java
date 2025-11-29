package com.mockio.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

/*
    웹 브라우저가 다른 도메인(origin)의 리소스에 접근할 수 있도록 허용할지 여부를 결정
 */
@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_ORIGINS  = List.of("http://localhost:3000");
    private static final List<String> ALLOWED_METHODS  = List.of("GET", "POST", "PUT", "DELETE");
    private static final List<String> ALLOWED_HEADERS  = List.of("Authorization", "Content-Type");
    private static final List<String> EXPOSED_HEADERS  = List.of("Authorization", "refresh-token");


    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //Origin 제한 (운영 시 실제 프론트 주소만 허용)
        corsConfiguration.setAllowedOrigins(ALLOWED_ORIGINS);
        //요청 메서드 제한
        corsConfiguration.setAllowedMethods(ALLOWED_METHODS);
        //요청 헤더 제한
        corsConfiguration.setAllowedHeaders(ALLOWED_HEADERS);
        //쿠키/헤더로 인증정보 전송 허용
        corsConfiguration.setAllowCredentials(true);
        //프론트가 응답 헤더(Access, Refresh Token)를 읽을 수 있도록 설정
        corsConfiguration.setExposedHeaders(EXPOSED_HEADERS );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
