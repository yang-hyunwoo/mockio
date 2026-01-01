package com.mockio.auth_service.kafka;

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
