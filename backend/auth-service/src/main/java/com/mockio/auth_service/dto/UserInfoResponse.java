package com.mockio.auth_service.dto;

public record UserInfoResponse(
        Long id,
        String email,
        String nickname
) {
}
