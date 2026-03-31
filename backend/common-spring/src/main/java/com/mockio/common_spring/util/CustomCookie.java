package com.mockio.common_spring.util;

import org.springframework.http.ResponseCookie;

public abstract class CustomCookie {

    /**
     * 쿠키 생성
     * @param cookieName
     * @param cookieValue
     * @param time
     * @return
     */
    public static ResponseCookie createCookie(String cookieName, String cookieValue, int time) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .maxAge(time)
                .path("/");

        if (EnvironmentProvider.isProd()) {
            builder.secure(true)
                    .sameSite("None");
        } else {
            builder.secure(false)
                    .sameSite("Lax");
        }

        return builder.build();
    }
    /**
     * 쿠키 삭제
     * @param name
     * @return
     */
    public static ResponseCookie deleteCookie(String name) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0);

        if (EnvironmentProvider.isProd()) {
            builder.secure(true)
                    .sameSite("None");
        } else {
            builder.secure(false)
                    .sameSite("Lax");
        }
        return builder.build();
    }
}
