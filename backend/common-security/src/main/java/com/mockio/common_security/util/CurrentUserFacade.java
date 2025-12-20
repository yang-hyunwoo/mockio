package com.mockio.common_security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class CurrentUserFacade<T> {

    private final CurrentUserPort<T> currentUserPort;

    public CurrentUserFacade(CurrentUserPort<T> currentUserPort) {
        this.currentUserPort = currentUserPort;
    }

    public T getCurrentUser(boolean required) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            if (required) {
                throw new UnauthorizedException("UNAUTHORIZED");
            }
            return null;
        }

        Object principal = authentication.getPrincipal();

        // OAuth2 Resource Server(JWT) 기준
        if (!(principal instanceof Jwt jwt)) {
            if (required) {
                throw new UnauthorizedException("UNAUTHORIZED_PRINCIPAL");
            }
            return null;
        }

        String keycloakId = jwt.getSubject();
        if (keycloakId == null || keycloakId.isBlank()) {
            if (required) {
                throw new UnauthorizedException("MISSING_SUBJECT");
            }
            return null;
        }

        return currentUserPort.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    if (required) {
                        throw new UnauthorizedException("USER_NOT_FOUND");
                    }
                    return null;
                });
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
