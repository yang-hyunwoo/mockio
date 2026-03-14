package com.mockio.user_service.dto.response;

public record UserIdResponse(
        Long userId,
        String keycloakUserId
) {
}

