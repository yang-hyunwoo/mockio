package com.mockio.auth_service.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import com.mockio.common_spring.constant.OutboxStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxAuthEnqueueServiceTest {

    @Mock OutboxAuthEventRepository outboxRepo;
    @Mock ObjectMapper objectMapper;

    @InjectMocks OutboxAuthEnqueueService service;

    @Test
    void enqueueKeycloakDisable_success_savesPending() {
        // given
        UUID eventId = UUID.randomUUID();
        String keycloakUserId = "kc-123";

        JsonNode payloadNode = mock(JsonNode.class);
        given(objectMapper.valueToTree(any())).willReturn(payloadNode);

        ArgumentCaptor<OutboxAuthEvent> captor = ArgumentCaptor.forClass(OutboxAuthEvent.class);

        // when
        service.enqueueKeycloakDisable(eventId, keycloakUserId, "USER_DELETED");

        // then
        then(outboxRepo).should().save(captor.capture());

        OutboxAuthEvent saved = captor.getValue();
        assertThat(saved.getEventType()).isEqualTo("KEYCLOAK_DISABLE_USER");
        assertThat(saved.getAggregateId()).isEqualTo(keycloakUserId);
        assertThat(saved.getIdempotencyKey()).isEqualTo("KEYCLOAK_DISABLE_USER:" + keycloakUserId);
        assertThat(saved.getStatus()).isEqualTo(OutboxStatus.PENDING);
        assertThat(saved.getMaxAttempts()).isEqualTo(10);
        assertThat(saved.getPayload()).isEqualTo(payloadNode);
    }

    @Test
    void enqueueKeycloakDisable_duplicate_ignores() {
        // given
        given(objectMapper.valueToTree(any())).willReturn(mock(JsonNode.class));
        given(outboxRepo.save(any())).willThrow(new DataIntegrityViolationException("dup"));

        // when (예외 없어야 함)
        service.enqueueKeycloakDisable(UUID.randomUUID(), "kc-123", "USER_DELETED");

        // then
        then(outboxRepo).should().save(any());
    }
}