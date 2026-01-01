package com.mockio.auth_service.kafka.service;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSucceeded(Long outboxId) {
        outboxRepo.markSucceeded(outboxId, OffsetDateTime.now());
    }

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
