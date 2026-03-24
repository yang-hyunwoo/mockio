package com.mockio.user_service.dto;

public record UserAuthInfoResponse(
        Long id,
        String email,
        String password,
        String role,
        int failLoginCount,
        String status
) {
}
