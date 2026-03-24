package com.mockio.core_service.feedback.kafka.dto.request;

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
) {}
