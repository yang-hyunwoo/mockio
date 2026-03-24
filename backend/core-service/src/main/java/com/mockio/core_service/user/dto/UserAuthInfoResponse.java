package com.mockio.core_service.user.dto;

public record UserAuthInfoResponse(
        Long id,
        String email,
        String password,
        String role,
        int failLoginCount,
        String status
) {
}
