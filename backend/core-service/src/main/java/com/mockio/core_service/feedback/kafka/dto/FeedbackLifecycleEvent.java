package com.mockio.core_service.feedback.kafka.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

public record FeedbackLifecycleEvent(
        UUID eventId,
        String aggregateType,
        Long aggregateId,
        String eventType,
        JsonNode payload,
        OffsetDateTime occurredAt
) {}
