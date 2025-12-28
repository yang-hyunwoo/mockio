package com.mockio.auth_service.dto.response;

public record RefreshResponse(String accessToken, Long expiresIn) {}
