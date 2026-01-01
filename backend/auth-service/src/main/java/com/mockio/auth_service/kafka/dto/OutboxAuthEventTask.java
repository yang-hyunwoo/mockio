package com.mockio.auth_service.kafka.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record OutboxAuthEventTask(
        Long id,
        String aggregateId,
        JsonNode payload,
        int attemptCount,
        int maxAttempts
) {}
