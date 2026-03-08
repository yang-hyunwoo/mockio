package com.mockio.feedback_service.config;

import com.mockio.feedback_service.handler.CusstomAccessDeniedHandler;
import com.mockio.feedback_service.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, InternalTokenFilter internalTokenFilter) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/feedback/v1/public/**").permitAll()
                        .requestMatchers("/api/feedback/v1/internal/**").permitAll()
                        // 나머지는 다 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        internalTokenFilter,
                        BearerTokenAuthenticationFilter.class
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CusstomAccessDeniedHandler()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

}
