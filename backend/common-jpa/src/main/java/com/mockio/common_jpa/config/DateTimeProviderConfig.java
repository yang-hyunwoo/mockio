package com.mockio.common_jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
public class DateTimeProviderConfig {

    @Bean("offSetDateTimeProvider")
    public DateTimeProvider offSetDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}