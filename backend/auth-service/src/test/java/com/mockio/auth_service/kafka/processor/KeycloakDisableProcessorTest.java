package com.mockio.auth_service.kafka.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.client.KeycloakUserClient;
import com.mockio.auth_service.kafka.dto.KeycloakDisableUserPayload;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import com.mockio.common_spring.constant.OutboxStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakDisableProcessorTest {

    @Mock ObjectMapper objectMapper;
    @Mock OutboxAuthEventRepository outboxRepo;
    @Mock KeycloakUserClient keycloakUserClient;

    @InjectMocks KeycloakDisableProcessor processor;

    @Test
    void process_success_calls_keycloak_and_marksSucceeded() throws Exception {
        // given
        Long outboxId = 1L;
        String json = "{\"any\":true}";
        KeycloakDisableUserPayload payload =
                new KeycloakDisableUserPayload(UUID.randomUUID(), "kc-123", "USER_DELETED");

        given(objectMapper.readValue(json, KeycloakDisableUserPayload.class)).willReturn(payload);

        // when
        processor.process(outboxId, json);

        // then
        then(keycloakUserClient).should().disableUser("kc-123");
        then(outboxRepo).should().markSucceeded(eq(outboxId), any(OffsetDateTime.class));
    }

    @Test
    void markFailedOrDead_whenExceedsMax_attempts_goesDead() {
        // given
        Long id = 99L;
        int currentAttempt = 9;
        int maxAttempts = 10;

        // when
        processor.markFailedOrDead(id, currentAttempt, maxAttempts, "err");

        // then
        then(outboxRepo).should().updateForRetryOrDead(
                eq(id),
                eq(OutboxStatus.DEAD),
                eq(10),
                any(OffsetDateTime.class),
                anyString(),
                any(OffsetDateTime.class)
        );
    }

    @Test
    void markFailedOrDead_whenNotExceed_schedulesRetry() {
        // given
        Long id = 100L;
        int currentAttempt = 0;
        int maxAttempts = 10;

        // when
        processor.markFailedOrDead(id, currentAttempt, maxAttempts, "err");

        // then
        then(outboxRepo).should().updateForRetryOrDead(
                eq(id),
                eq(OutboxStatus.FAILED),
                eq(1),
                any(OffsetDateTime.class),
                anyString(),
                any(OffsetDateTime.class)
        );
    }
}