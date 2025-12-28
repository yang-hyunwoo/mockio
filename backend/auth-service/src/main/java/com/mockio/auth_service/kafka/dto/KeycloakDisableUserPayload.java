package com.mockio.auth_service.kafka.dto;

import java.util.UUID;

public record KeycloakDisableUserPayload(
        UUID eventId,
        String keycloakUserId,
        String reason
) {}
