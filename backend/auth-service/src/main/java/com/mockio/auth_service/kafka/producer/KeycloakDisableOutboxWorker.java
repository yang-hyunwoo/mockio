package com.mockio.auth_service.kafka.producer;

/**
 * Keycloak 사용자 비활성화 Outbox 이벤트를 처리하는 워커 컴포넌트.
 *
 * <p>Outbox 패턴을 기반으로 주기적으로 미처리 이벤트를 조회하여,
 * 외부 시스템(Keycloak Admin API)을 호출하고
 * 처리 결과를 Outbox 상태로 반영한다.</p>
 *
 * <p>처리 흐름은 다음과 같이 단계적으로 분리된다:
 * <ol>
 *   <li>TX: 처리 대상 조회 + PROCESSING 상태 마킹</li>
 *   <li>No TX: 외부 시스템 호출</li>
 *   <li>REQUIRES_NEW TX: 성공/실패 결과 반영</li>
 * </ol>
 *
 * <p>각 워커는 고유 workerId를 가지며,
 * 동시 실행 환경에서도 중복 처리를 방지한다.</p>
 */

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

    /**
     * 주기적으로 Outbox 이벤트를 폴링하여 Keycloak 비활성화 작업을 수행한다.
     *
     * <p>fixedDelay 방식으로 실행되며,
     * 한 번의 실행에서 최대 지정된 개수의 이벤트를 처리한다.</p>
     *
     * <p>P6Spy 로그를 일시적으로 비활성화하여
     * 대량 처리 시 불필요한 SQL 로그를 줄인다.</p>
     */
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
