package com.mockio.auth_service.kafka.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.auth_service.kafka.dto.OutboxAuthEventTask;
import com.mockio.auth_service.kafka.processor.KeycloakDisableProcessor;
import com.mockio.auth_service.kafka.service.OutboxResultService;
import com.mockio.auth_service.kafka.service.OutboxTxService;
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

    @Mock OutboxTxService outboxTxService;
    @Mock KeycloakDisableProcessor processor;
    @Mock OutboxResultService outboxResultService;
    @Mock ObjectMapper ObjectMapper;
    KeycloakDisableOutboxWorker worker;
    JsonNode payload;
    JsonNode payload2;
    @BeforeEach
    void setUp() throws JsonProcessingException {
        worker = new KeycloakDisableOutboxWorker(outboxTxService, processor, outboxResultService);
        ObjectMapper = new ObjectMapper();

        payload = ObjectMapper.readTree("""
            {
              "eventId": "x",
              "keycloakUserId": "kc-123",
              "reason": "USER_DELETED"
            }
            """);
        payload2 = ObjectMapper.readTree("""
            {
              "eventId": "x",
              "keycloakUserId": "kc-999",
              "reason": "USER_DELETED"
            }
            """);
        // workerId가 필드로 있다면 테스트에서 고정 (필드명이 다르면 바꾸세요)
        ReflectionTestUtils.setField(worker, "workerId", "auth-test-worker");
    }

    @Test
    void run_fetchesTasks_callsProcessor_thenMarksSucceeded() throws JsonProcessingException {

        // given
        OutboxAuthEventTask task = new OutboxAuthEventTask(
                1L,
                "kc-123",
                payload,
                0,
                10
        );

        given(outboxTxService.fetchAndMarkProcessing(100, "auth-test-worker"))
                .willReturn(List.of(task));

        // when
        worker.run();

        // then
        then(processor).should().callExternal(task.payload().toString());
        then(outboxResultService).should().markSucceeded(eq(1L));
        then(outboxResultService).should(never()).markFailedOrDead(anyLong(), anyInt(), anyInt(), anyString());
    }

    @Test
    void run_whenProcessorThrows_marksFailedOrDead() {
        // given
        OutboxAuthEventTask task = new OutboxAuthEventTask(
                2L,
                "kc-999",
                payload2,
                0,
                10
        );

        given(outboxTxService.fetchAndMarkProcessing(100, "auth-test-worker"))
                .willReturn(List.of(task));

        willThrow(new RuntimeException("boom"))
                .given(processor).callExternal(task.payload().toString());

        // when
        worker.run();

        // then
        then(outboxResultService).should().markFailedOrDead(
                eq(2L),
                eq(0),
                eq(10),
                contains("boom")
        );
        then(outboxResultService).should(never()).markSucceeded(anyLong());
    }
}
