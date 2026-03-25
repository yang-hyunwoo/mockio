package com.mockio.auth_service.dto.response;

public record UserAuthInfoResponse(
        Long id,
        String email,
        String name,
        String password,
        String role,
        int failLoginCount,
        String status
) {
}
