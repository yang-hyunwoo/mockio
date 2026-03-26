package com.mockio.core_service.user.util;

import jakarta.servlet.http.HttpServletRequest;

public final class IpUtils {

    private IpUtils() {
    }

    public static String extractClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}