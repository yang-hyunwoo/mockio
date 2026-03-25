package com.mockio.core_service.user.dto.response;

public record UserInfoResponse(
        Long id,
        String email,
        String nickname
) {}
