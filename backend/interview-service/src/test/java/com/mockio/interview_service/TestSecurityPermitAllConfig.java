package com.mockio.interview_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityPermitAllConfig {

    @Bean
    SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/users/v1/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                // syncMyProfile 테스트에서 jwt()가 이미 잘 동작 중이므로 보통 이 설정이 이미 들어와 있을 가능성이 큽니다.
                // 그래도 안전하게 명시해두려면 아래를 켜두세요.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}
