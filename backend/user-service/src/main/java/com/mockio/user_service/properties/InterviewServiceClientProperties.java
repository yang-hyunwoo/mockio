package com.mockio.user_service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.interview")
public record InterviewServiceClientProperties(
        String baseUrl,
        int connectTimeoutMs,
        int readTimeoutMs
) {
}
