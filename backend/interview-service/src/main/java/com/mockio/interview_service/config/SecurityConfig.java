package com.mockio.interview_service.config;

import com.mockio.interview_service.handler.CusstomAccessDeniedHandler;
import com.mockio.interview_service.handler.CustomAuthenticationEntryPoint;
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
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RequestArriveLogFilter requestArriveLogFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, InternalTokenFilter internalTokenFilter) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/interview/v1/public/**").permitAll()
                        .requestMatchers("/api/interview/v1/internal/**").permitAll()
                        // 나머지는 다 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(requestArriveLogFilter, SecurityContextHolderFilter.class)
                .addFilterBefore(
                        internalTokenFilter,
                        BearerTokenAuthenticationFilter.class
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CusstomAccessDeniedHandler()))
                .oauth2ResourceServer(oauth -> oauth.jwt())
                .build();
    }

}
