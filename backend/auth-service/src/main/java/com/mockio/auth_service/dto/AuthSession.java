package com.mockio.auth_service.dto;

public record AuthSession(
        String refreshToken,
        String accessToken,
        long accessExpiresAtEpochSec
) {}
