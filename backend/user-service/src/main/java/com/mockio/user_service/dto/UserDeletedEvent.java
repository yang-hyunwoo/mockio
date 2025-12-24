package com.mockio.user_service.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDeletedEvent(
        UUID eventId,
        String eventType,
        OffsetDateTime occurredAt,
        Long userId,
        String keycloakId
) {
    public static UserDeletedEvent of(Long userId, String keycloakId) {
        return new UserDeletedEvent(
                UUID.randomUUID(),
                "USER_DELETED",
                OffsetDateTime.now(),
                userId,
                keycloakId
        );
    }
}
