package com.mockio.auth_service.dto.response;

public record TokenResponse(
        String accessToken,
        Long expiresIn,
        String tokenType
) {}
