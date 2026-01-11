package com.mockio.auth_service.kafka;

/**
 * Auth Service Outbox 이벤트 적재를 담당하는 서비스.
 *
 * <p>사용자 라이프사이클 이벤트에 따라 발생한
 * Keycloak 사용자 비활성화 요청을 Outbox 테이블에 적재한다.</p>
 *
 * <p>멱등성 보장을 위해 idempotencyKey를 사용하며,
 * 중복 요청 시에는 기존 이벤트를 재사용한다.</p>
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.constant.EnqueueResult;
import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.auth_service.kafka.dto.KeycloakDisableUserPayload;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import com.mockio.common_spring.constant.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxAuthEnqueueService {

    private final OutboxAuthEventRepository outboxRepo;

    private final ObjectMapper objectMapper;

    /**
     * Keycloak 사용자 비활성화 요청을 Outbox에 적재한다.
     *
     * <p>사전 존재 여부 조회 + DB 유니크 제약 위반 감지를 통해
     * 중복 적재를 방지하는 멱등성 전략을 적용한다.</p>
     *
     * <p>Outbox 이벤트는 NEW 상태로 저장되며,
     * 이후 워커에 의해 비동기적으로 처리된다.</p>
     *
     * @param eventId 상위 도메인 이벤트 ID
     * @param keycloakUserId 비활성화 대상 Keycloak 사용자 ID
     * @param reason 비활성화 사유
     * @return 적재 결과 (ENQUEUED / ALREADY_ENQUEUED)
     */
    @Transactional
    public EnqueueResult enqueueKeycloakDisable(UUID eventId, String keycloakUserId, String reason) {
        String idempotencyKey =  "KEYCLOAK_DISABLE_USER:" + keycloakUserId;

        // 1) 사전 체크
        if (outboxRepo.existsByIdempotencyKey(idempotencyKey)) {
            return EnqueueResult.ALREADY_ENQUEUED;
        }

        KeycloakDisableUserPayload payload = new KeycloakDisableUserPayload(eventId, keycloakUserId, reason);
        JsonNode jsonNode = objectMapper.valueToTree(payload);

        try {
            outboxRepo.save(OutboxAuthEvent.builder()
                    .eventType("KEYCLOAK_DISABLE_USER")
                    .aggregateId(keycloakUserId)
                    .idempotencyKey(idempotencyKey)
                    .payload(jsonNode)
                    .status(OutboxStatus.NEW)
                    .maxAttempts(10)
                    .nextAttemptAt(OffsetDateTime.now())
                    .build());

            return EnqueueResult.ENQUEUED;

        } catch (DataIntegrityViolationException e) {
            if (isUniqueViolation(e)) {
                // 멱등: 이미 누군가 먼저 적재함
                return EnqueueResult.ALREADY_ENQUEUED;
            }
            throw e;
        }
    }

    /**
     * DataIntegrityViolationException이 유니크 제약 위반인지 판단한다.
     *
     * <p>PostgreSQL SQLState(23505)를 기준으로 유니크 제약 위반을 판별한다.</p>
     *
     * @param e 발생한 DataIntegrityViolationException
     * @return 유니크 제약 위반이면 true
     */
    private boolean isUniqueViolation(DataIntegrityViolationException e) {
        Throwable t = e;
        while (t != null) {
            // Postgres의 경우 org.postgresql.util.PSQLException
            if (t.getClass().getName().equals("org.postgresql.util.PSQLException")) {
                try {
                    String sqlState = (String) t.getClass().getMethod("getSQLState").invoke(t);
                    return "23505".equals(sqlState); //unique_violation
                } catch (Exception ignore) {
                    return false;
                }
            }
            t = t.getCause();
        }
        return false;
    }

}
