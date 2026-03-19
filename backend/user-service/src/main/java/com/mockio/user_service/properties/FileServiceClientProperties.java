package com.mockio.user_service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.file")
public record FileServiceClientProperties(
        String baseUrl
) {
}
