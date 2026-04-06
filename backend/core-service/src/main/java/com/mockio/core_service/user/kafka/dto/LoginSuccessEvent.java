package com.mockio.core_service.user.kafka.dto;

import java.time.OffsetDateTime;

public record LoginSuccessEvent(
        Long userId,
        OffsetDateTime occurredAt
) {}
