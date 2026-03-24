package com.mockio.core_service.user.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.file")
public record FileServiceClientProperties(
        String baseUrl
) {
}
