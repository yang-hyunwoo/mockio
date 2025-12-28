package com.mockio.user_service.kafka.config;

import com.mockio.common_spring.constant.OutboxStatus;
import com.mockio.user_service.kafka.repository.OutboxUserEventRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OutboxMetricsConfig {

    private final OutboxUserEventRepository repo;

    @Bean
    public MeterBinder outboxMeters() {
        return registry -> {
            Gauge.builder("outbox.user_events.pending", repo,
                            r -> r.countByStatus(OutboxStatus.PENDING))
                    .register(registry);

            Gauge.builder("outbox.user_events.processing", repo,
                            r -> r.countByStatus(OutboxStatus.PROCESSING))
                    .register(registry);

            Gauge.builder("outbox.user_events.dead", repo,
                            r -> r.countByStatus(OutboxStatus.DEAD))
                    .register(registry);
        };
    }
}
