package com.mockio.auth_service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.user")
public record UserServiceClientProperties(
        String baseUrl,
        int connectTimeoutMs,
        int readTimeoutMs
) {
}
