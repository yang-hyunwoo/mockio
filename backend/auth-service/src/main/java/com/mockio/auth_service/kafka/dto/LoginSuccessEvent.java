package com.mockio.auth_service.kafka.dto;

import java.time.OffsetDateTime;

public record LoginSuccessEvent(
        Long userId,
        OffsetDateTime occurredAt
) {}
