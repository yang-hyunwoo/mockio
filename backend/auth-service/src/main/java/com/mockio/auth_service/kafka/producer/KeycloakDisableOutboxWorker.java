package com.mockio.auth_service.kafka.producer;

import com.mockio.auth_service.kafka.dto.OutboxAuthEventTask;
import com.mockio.auth_service.kafka.processor.KeycloakDisableProcessor;
import com.mockio.auth_service.kafka.service.OutboxResultService;
import com.mockio.auth_service.kafka.service.OutboxTxService;
import com.mockio.auth_service.util.P6SpyLogToggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class KeycloakDisableOutboxWorker {

    private final OutboxTxService txService;
    private final KeycloakDisableProcessor processor;
    private final OutboxResultService resultService;

    private final String workerId;

    public KeycloakDisableOutboxWorker(
            OutboxTxService txService,
            KeycloakDisableProcessor processor,
            OutboxResultService resultService
    ) {
        this.txService = txService;
        this.processor = processor;
        this.resultService = resultService;
        this.workerId = "auth-" + UUID.randomUUID();
    }

    @Scheduled(fixedDelay = 10_000)
    public void run() {
        P6SpyLogToggle.withoutP6Spy(() -> {
            // 1단계 (TX): due 조회 + PROCESSING 마킹 후 Task 목록 반환
            List<OutboxAuthEventTask> tasks = txService.fetchAndMarkProcessing(100, workerId);
            if (tasks.isEmpty()) return;

            // 2단계 (No TX): 외부 호출
            for (OutboxAuthEventTask task : tasks) {
                try {
                    processor.callExternal(task.payload().toString());

                    // 3단계 (REQUIRES_NEW): 성공 업데이트
                    resultService.markSucceeded(task.id());

                } catch (Exception ex) {
                    log.warn("Keycloak disable failed. outboxId={}, aggregateId={}", task.id(), task.aggregateId(), ex);

                    // 3단계 (REQUIRES_NEW): 실패/DEAD 업데이트
                    resultService.markFailedOrDead(
                            task.id(),
                            task.attemptCount(),
                            task.maxAttempts(),
                            ex.getMessage()
                    );
                }
            }
        });
    }
}
