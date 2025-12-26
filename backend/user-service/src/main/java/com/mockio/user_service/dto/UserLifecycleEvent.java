package com.mockio.user_service.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserLifecycleEvent(
        UUID eventId,
        String aggregateType,
        Long aggregateId,
        String eventType,
        JsonNode payload,
        OffsetDateTime occurredAt
) {
}
