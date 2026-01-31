package com.mockio.feedback_service.kafka.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InterviewLifecycleEvent(
        UUID eventId,
        String aggregateType,
        Long aggregateId,
        String eventType,
        JsonNode payload,
        OffsetDateTime occurredAt
) {
}
