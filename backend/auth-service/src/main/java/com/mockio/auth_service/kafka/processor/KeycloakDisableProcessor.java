package com.mockio.auth_service.kafka.processor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.client.KeycloakUserClient;
import com.mockio.auth_service.kafka.dto.KeycloakDisableUserPayload;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import com.mockio.auth_service.kafka.util.OutboxBackoffPolicy;
import com.mockio.common_spring.constant.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class KeycloakDisableProcessor {

    private final ObjectMapper objectMapper;
    private final OutboxAuthEventRepository outboxRepo;
    private final KeycloakUserClient keycloakUserClient; // 현우님이 만든 client

    @Transactional
    public void process(Long outboxId, String payloadJson) {
        KeycloakDisableUserPayload payload = parse(payloadJson);

        try {
            keycloakUserClient.disableUser(payload.keycloakUserId());
            outboxRepo.markSucceeded(outboxId, OffsetDateTime.now());
        } catch (Exception ex) {
            // 다음 시도 계산
            OffsetDateTime now = OffsetDateTime.now();

            // 현재 attempt_count를 조회하지 않고 update에 넣으려면, 호출부에서 attemptCount를 들고 와야 합니다.
            // 여기서는 워커에서 attemptCount를 함께 넘기도록 구성하는 편이 정확합니다.
            throw ex;
        }
    }

    public KeycloakDisableUserPayload parse(String payloadJson) {
        try {
            return objectMapper.readValue(payloadJson, KeycloakDisableUserPayload.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid payload json", e);
        }
    }

    public void markFailedOrDead(Long id, int currentAttemptCount, int maxAttempts, String error) {
        int nextAttemptCount = currentAttemptCount + 1;
        OffsetDateTime now = OffsetDateTime.now();

        if (nextAttemptCount >= maxAttempts) {
            outboxRepo.updateForRetryOrDead(
                    id,
                    OutboxStatus.DEAD,
                    nextAttemptCount,
                    now, // 의미 없지만 필수 컬럼이라 now
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
