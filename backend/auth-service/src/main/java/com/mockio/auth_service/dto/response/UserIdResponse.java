package com.mockio.auth_service.dto.response;

public record UserIdResponse(
        Long userId,
        String keycloakUserId
) {
}
