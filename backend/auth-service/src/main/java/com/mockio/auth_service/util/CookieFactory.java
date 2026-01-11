package com.mockio.auth_service.util;

/**
 * Refresh Token 쿠키 생성을 담당하는 팩토리 컴포넌트.
 *
 * <p>보안 관련 속성(HttpOnly, Secure, SameSite, Path, Domain)을
 * 외부 설정으로부터 주입받아 일관된 쿠키 정책을 적용한다.</p>
 *
 * <p>Refresh Token은 클라이언트 스크립트에서 접근할 수 없도록
 * HttpOnly 쿠키로만 발급된다.</p>
 */

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
            @Value("${security.cookie.refresh-name:refresh_token}")
            String refreshName,

            @Value("${security.cookie.refresh-path:/api/auth/v1}")
            String refreshPath,

            @Value("${security.cookie.secure:true}")
            boolean secure,

            @Value("${security.cookie.same-site:Lax}")
            String sameSite,

            @Value("${security.cookie.domain:}")
            String domain
    ) {
        this.refreshName = refreshName;
        this.refreshPath = refreshPath;
        this.secure = secure;
        this.sameSite = sameSite;
        this.domain = domain;
    }

    /**
     * Refresh Token용 HttpOnly 쿠키를 생성한다.
     *
     * <p>보안 설정은 application 설정 값을 따르며,
     * 토큰 만료 시간(maxAgeSeconds)에 맞춰 쿠키 만료 시간도 설정한다.</p>
     *
     * @param refreshToken Refresh Token 값
     * @param maxAgeSeconds 쿠키 유효 시간(초)
     * @return 생성된 ResponseCookie
     */
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
