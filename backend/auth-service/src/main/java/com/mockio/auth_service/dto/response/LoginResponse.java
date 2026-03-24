package com.mockio.auth_service.dto.response;

public record LoginResponse(
        Long id,
        String email,
        String accessToken

) {
}
