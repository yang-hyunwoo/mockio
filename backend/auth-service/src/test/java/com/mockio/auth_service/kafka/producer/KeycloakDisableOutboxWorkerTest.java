package com.mockio.auth_service.kafka.producer;


import com.fasterxml.jackson.databind.JsonNode;
import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.auth_service.kafka.processor.KeycloakDisableProcessor;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import com.mockio.common_spring.constant.OutboxStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakDisableOutboxWorkerTest {

    @Mock OutboxAuthEventRepository outboxRepo;
    @Mock KeycloakDisableProcessor processor;

    KeycloakDisableOutboxWorker worker;

    @BeforeEach
    void setUp() {
        // 테스트용 workerId 고정
        worker = new KeycloakDisableOutboxWorker(outboxRepo, processor, "auth-test-worker");
    }

    @Test
    void run_picksDueEvents_marksProcessing_thenProcesses() {
        // given
        OutboxAuthEvent e = OutboxAuthEvent.builder()
                .eventType("KEYCLOAK_DISABLE_USER")
                .aggregateId("kc-123")
                .idempotencyKey("KEYCLOAK_DISABLE_USER:kc-123")
                .payload(mock(JsonNode.class))
                .status(OutboxStatus.PENDING)
                .maxAttempts(10)
                .nextAttemptAt(OffsetDateTime.now().minusSeconds(1))
                .build();

        ReflectionTestUtils.setField(e, "id", 1L);

        given(outboxRepo.lockTop100Due()).willReturn(List.of(e));

        // when
        worker.run();

        // then
        then(outboxRepo).should().markProcessing(eq(1L), eq("auth-test-worker"), any(OffsetDateTime.class));
        then(processor).should().process(eq(1L), anyString());
    }

    @Test
    void run_whenProcessorThrows_marksFailedOrDead() {
        // given
        OutboxAuthEvent e = OutboxAuthEvent.builder()
                .eventType("KEYCLOAK_DISABLE_USER")
                .aggregateId("kc-999")
                .idempotencyKey("KEYCLOAK_DISABLE_USER:kc-999")
                .payload(mock(JsonNode.class))
                .status(OutboxStatus.PENDING)
                .maxAttempts(10)
                .nextAttemptAt(OffsetDateTime.now().minusSeconds(1))
                .build();

        ReflectionTestUtils.setField(e, "id", 2L);
        ReflectionTestUtils.setField(e, "attemptCount", 0);
        ReflectionTestUtils.setField(e, "maxAttempts", 10);

        given(outboxRepo.lockTop100Due()).willReturn(List.of(e));
        willThrow(new RuntimeException("boom")).given(processor).process(eq(2L), anyString());

        // when
        worker.run();

        // then
        then(processor).should().markFailedOrDead(eq(2L), eq(0), eq(10), contains("boom"));
    }
}