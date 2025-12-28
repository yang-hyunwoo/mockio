package com.mockio.auth_service.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void enqueueKeycloakDisable(UUID eventId, String keycloakUserId, String reason) {
        String idempotencyKey =  "KEYCLOAK_DISABLE_USER:" + keycloakUserId;

        KeycloakDisableUserPayload payload = new KeycloakDisableUserPayload(eventId, keycloakUserId, reason);
        JsonNode jsonNode = objectMapper.valueToTree(payload);

        try {
            outboxRepo.save(OutboxAuthEvent.builder()
                    .eventType("KEYCLOAK_DISABLE_USER")
                    .aggregateId(keycloakUserId)
                    .idempotencyKey(idempotencyKey)
                    .payload(jsonNode)
                    .status(OutboxStatus.PENDING)
                    .maxAttempts(10)
                    .nextAttemptAt(OffsetDateTime.now())
                    .build());
        } catch (DataIntegrityViolationException e) {
            // 동일 유저 disable 작업이 이미 적재된 경우 -> 멱등 처리
        }
    }

}
