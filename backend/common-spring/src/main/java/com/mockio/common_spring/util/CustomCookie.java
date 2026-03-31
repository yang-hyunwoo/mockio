package com.mockio.common_spring.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomCookie {

    private final EnvironmentProvider environmentProvider;

    public ResponseCookie createCookie(String cookieName, String cookieValue, int time) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .maxAge(time)
                .path("/");

        if (environmentProvider.isProd()) {
            builder.secure(true)
                    .sameSite("None");
        } else {
            builder.secure(false)
                    .sameSite("Lax");
        }

        return builder.build();
    }

    public ResponseCookie deleteCookie(String name) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0);

        if (environmentProvider.isProd()) {
            builder.secure(true)
                    .sameSite("None");
        } else {
            builder.secure(false)
                    .sameSite("Lax");
        }

        return builder.build();
    }
}
