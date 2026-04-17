package com.mockio.support_service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.core")
public record CoreServiceClientProperties(
        String baseUrl
) {}
