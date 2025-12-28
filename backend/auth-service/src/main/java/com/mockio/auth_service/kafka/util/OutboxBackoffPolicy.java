package com.mockio.auth_service.kafka.util;

import java.time.Duration;

public class OutboxBackoffPolicy {

    public static Duration nextDelay(int nextAttemptCount) {
        return switch (nextAttemptCount) {
            case 1 -> Duration.ofMinutes(1);
            case 2 -> Duration.ofMinutes(5);
            case 3 -> Duration.ofMinutes(30);
            case 4 -> Duration.ofHours(2);
            case 5 -> Duration.ofHours(6);
            case 6 -> Duration.ofHours(12);
            default -> Duration.ofHours(24);
        };
    }
}
