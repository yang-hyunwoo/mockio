package com.mockio.common_security.config;

import com.mockio.common_jpa.AuditorPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("system");
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof AuditorPrincipal auditorPrincipal) {
                return Optional.of(String.valueOf(auditorPrincipal.getUserId()));
            }

            return Optional.ofNullable(authentication.getName())
                    .filter(name -> !name.isBlank())
                    .filter(name -> !"anonymousUser".equals(name))
                    .or(() -> Optional.of("system"));
        };
    }

}