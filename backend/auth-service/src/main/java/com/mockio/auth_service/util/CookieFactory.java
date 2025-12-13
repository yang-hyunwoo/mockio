package com.mockio.auth_service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieFactory {

    private final String refreshName;
    private final String refreshPath;
    private final boolean secure;
    private final String sameSite;
    private final String domain;

    public CookieFactory(
            @Value("${security.cookie.refresh-name:refresh_token}") String refreshName,
            @Value("${security.cookie.refresh-path:/auth/refresh}") String refreshPath,
            @Value("${security.cookie.secure:true}") boolean secure,
            @Value("${security.cookie.same-site:Lax}") String sameSite,
            @Value("${security.cookie.domain:}") String domain
    ) {
        this.refreshName = refreshName;
        this.refreshPath = refreshPath;
        this.secure = secure;
        this.sameSite = sameSite;
        this.domain = domain;
    }

    public ResponseCookie refreshCookie(String refreshToken, long maxAgeSeconds) {
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(refreshName, refreshToken)
                .httpOnly(true)
                .secure(secure)
                .path(refreshPath)
                .sameSite(sameSite)
                .maxAge(maxAgeSeconds);

        if (domain != null && !domain.isBlank()) {
            responseCookieBuilder.domain(domain);
        }

        return responseCookieBuilder.build();
    }

    public String refreshCookieName() {
        return refreshName;
    }
}
