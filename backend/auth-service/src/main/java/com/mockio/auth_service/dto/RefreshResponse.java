package com.mockio.auth_service.dto;

public record RefreshResponse(String accessToken, Long expiresIn) {}
