package com.mockio.user_service.dto.response;

public record UserInfoResponse(
        Long id,
        String email,
        String nickname
) {
}
