package com.mockio.auth_service.kafka.service;

/**
 * Outbox 이벤트 처리 결과를 반영하는 서비스.
 *
 * <p>외부 시스템 호출 결과에 따라 Outbox 이벤트의 상태를
 * SENT, FAILED, DEAD로 전이시키며,
 * 재시도 횟수 및 다음 실행 시각을 함께 관리한다.</p>
 *
 * <p>외부 호출 실패가 전체 트랜잭션에 영향을 주지 않도록
 * 모든 상태 변경은 REQUIRES_NEW 트랜잭션으로 수행된다.</p>
 */

import com.mockio.auth_service.kafka.util.OutboxBackoffPolicy;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import com.mockio.common_spring.constant.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class OutboxResultService {

    private final OutboxAuthEventRepository outboxRepo;

    /**
     * Outbox 이벤트를 성공(SENT) 상태로 전환한다.
     *
     * <p>처리 완료 시각을 기록하고,
     * 해당 이벤트는 이후 재처리 대상에서 제외된다.</p>
     *
     * @param outboxId 처리 완료된 Outbox 이벤트 ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSucceeded(Long outboxId) {
        outboxRepo.markSucceeded(outboxId, OffsetDateTime.now());
    }

    /**
     * Outbox 이벤트 처리 실패를 기록하고 재시도 또는 DEAD 상태로 전환한다.
     *
     * <p>현재 시도 횟수와 최대 시도 횟수를 비교하여,
     * 재시도가 가능한 경우 FAILED 상태로 전환하고
     * 다음 재시도 시각을 백오프 정책에 따라 계산한다.</p>
     *
     * <p>최대 시도 횟수를 초과한 경우,
     * 이벤트를 DEAD 상태로 전환하여 더 이상 처리하지 않는다.</p>
     *
     * @param id Outbox 이벤트 ID
     * @param currentAttemptCount 현재 시도 횟수
     * @param maxAttempts 최대 시도 횟수
     * @param error 실패 원인 메시지
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailedOrDead(Long id, int currentAttemptCount, int maxAttempts, String error) {
        int nextAttemptCount = currentAttemptCount + 1;
        OffsetDateTime now = OffsetDateTime.now();

        if (nextAttemptCount >= maxAttempts) {
            outboxRepo.updateForRetryOrDead(
                    id,
                    OutboxStatus.DEAD,
                    nextAttemptCount,
                    now,        // 의미 없지만 NOT NULL이면 채움
                    error,
                    now
            );
            return;
        }

        OffsetDateTime nextAt = now.plus(OutboxBackoffPolicy.nextDelay(nextAttemptCount));
        outboxRepo.updateForRetryOrDead(
                id,
                OutboxStatus.FAILED,
                nextAttemptCount,
                nextAt,
                error,
                now
        );
    }

}
