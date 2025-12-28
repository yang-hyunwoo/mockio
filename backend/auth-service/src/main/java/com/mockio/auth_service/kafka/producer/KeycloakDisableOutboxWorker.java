package com.mockio.auth_service.kafka.producer;

import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.auth_service.kafka.processor.KeycloakDisableProcessor;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class KeycloakDisableOutboxWorker {

    private final OutboxAuthEventRepository outboxRepo;
    private final KeycloakDisableProcessor processor;

    private final String workerId ;

    @Autowired
    public KeycloakDisableOutboxWorker(OutboxAuthEventRepository outboxRepo,
                                       KeycloakDisableProcessor processor) {
        this.outboxRepo = outboxRepo;
        this.processor = processor;
        this.workerId = "auth-" + UUID.randomUUID();
    }

    // 테스트에서만 직접 new 로 호출
    KeycloakDisableOutboxWorker(OutboxAuthEventRepository outboxRepo,
                                KeycloakDisableProcessor processor,
                                String workerId) {
        this.outboxRepo = outboxRepo;
        this.processor = processor;
        this.workerId = workerId;
    }

    @Scheduled(fixedDelay = 10_000)
    @Transactional
    public void run() {
        List<OutboxAuthEvent> due = outboxRepo.lockTop100Due();
        if (due.isEmpty()) return;

        OffsetDateTime now = OffsetDateTime.now();

        for (OutboxAuthEvent e : due) {
            outboxRepo.markProcessing(e.getId(), workerId, now);

            try {
                processor.process(e.getId(), e.getPayload().toString());
            } catch (Exception ex) {
                log.warn("Keycloak disable failed. outboxId={}, aggregateId={}",
                        e.getId(), e.getAggregateId(), ex);

                processor.markFailedOrDead(
                        e.getId(),
                        e.getAttemptCount(),
                        e.getMaxAttempts(),
                        ex.getMessage()
                );
            }
        }
    }
}
