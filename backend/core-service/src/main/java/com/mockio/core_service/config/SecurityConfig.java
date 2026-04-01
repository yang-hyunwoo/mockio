package com.mockio.core_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.core_service.util.handler.CusstomAccessDeniedHandler;
import com.mockio.core_service.util.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, InternalTokenFilter internalTokenFilter) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                        .requestMatchers("/actuator/prometheus").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/health/**").permitAll()
                        .requestMatchers("/api/feedback/v1/public/**").permitAll()
                        .requestMatchers("/api/feedback/v1/internal/**").permitAll()
                        .requestMatchers("/api/interview/v1/public/**").permitAll()
                        .requestMatchers("/api/interview/v1/internal/**").permitAll()
                        .requestMatchers("/api/users/v1/public/**").permitAll()
                        .requestMatchers("/api/users/v1/internal/**").permitAll()
                        .requestMatchers("/api/ai/v1/**").permitAll()
                        // 나머지는 다 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        internalTokenFilter,
                        BearerTokenAuthenticationFilter.class
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new CusstomAccessDeniedHandler()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) //JWT Bearer Token을 표준 방식으로 검증하겠다
                .build();
    }

}
