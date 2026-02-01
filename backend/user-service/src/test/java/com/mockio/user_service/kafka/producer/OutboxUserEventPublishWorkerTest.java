package com.mockio.user_service.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_spring.constant.OutboxStatus;
import com.mockio.user_service.kafka.domain.OutboxUserEvent;
import com.mockio.user_service.kafka.repository.OutboxUserEventRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.shaded.com.google.common.util.concurrent.ListenableFuture;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OutboxUserEventPublishWorkerTest {
    @Mock OutboxUserEventRepository outboxRepository;
    @Mock KafkaTemplate<String, String> kafkaTemplate;
    @Mock ObjectMapper objectMapper;

    @InjectMocks OutboxUserEventPublishWorker worker;

    private OutboxUserEvent event;
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        event = OutboxUserEvent.createNew(
                UUID.randomUUID(),
                10L,
                "USER_DELETED",
                objectMapper.valueToTree(Map.of("a", 1))
        );
        // publishOne은 id로 조회하므로, 엔티티 id를 테스트에서 주입
        ReflectionTestUtils.setField(event, "id", id);
        ReflectionTestUtils.setField(event, "nextAttemptAt", OffsetDateTime.now().minusSeconds(10));
    }

    @Test
    void publishOne_success_marksSent() throws Exception {
        given(outboxRepository.findById(id)).willReturn(Optional.of(event));
        event.markProcessing("test-worker");
        // json이 null이 되지 않도록 강제
        doReturn("{\"ok\":true}").when(objectMapper).writeValueAsString(any());

        ProducerRecord<String, String> record = new ProducerRecord<>("user.lifecycle", "10", "{\"ok\":true}");
        SendResult<String, String> sendResult = new SendResult<>(record, null);

        given(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .willReturn(CompletableFuture.completedFuture(sendResult));

        worker.publishOne("test-worker",id);

        then(kafkaTemplate).should().send(anyString(), anyString(), anyString());

        assertThat(event.getStatus()).isEqualTo(OutboxStatus.SENT);
        assertThat(event.getSentAt()).isNotNull();
        assertThat(event.getLastError()).isNull();
    }

    @Test
    void publishOne_failed_incrementsAttempt_andSchedulesRetry() throws Exception {
        // given
        given(outboxRepository.findById(id)).willReturn(Optional.of(event));

        given(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .willReturn(CompletableFuture.failedFuture(new RuntimeException("kafka down")));

        event.markProcessing("test-worker");
        OffsetDateTime before = event.getNextAttemptAt();
        int beforeAttempt = event.getAttemptCount();

        // when
        worker.publishOne("test-worker",id);

        // then
        assertThat(event.getAttemptCount()).isEqualTo(beforeAttempt + 1);
        assertThat(event.getStatus()).isEqualTo(OutboxStatus.PENDING); // maxAttempts 미만이면 PENDING으로 복귀
        assertThat(event.getNextAttemptAt()).isAfter(before);          // nextAttemptAt가 미래로 밀려야 함
        assertThat(event.getLastError()).isNotBlank();
    }

    @Test
    void publishOne_reachesMaxAttempts_goesDead() throws Exception {
        // given
        // attemptCount를 9로 만들어서 다음 실패에 DEAD가 되도록 유도 (MAX_ATTEMPTS=10)
        ReflectionTestUtils.setField(event, "attemptCount", 9);
        event.markProcessing("test-worker");

        given(outboxRepository.findById(id)).willReturn(Optional.of(event));

        given(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .willReturn(CompletableFuture.failedFuture(new RuntimeException("still down")));

        // when
        worker.publishOne("test-worker",id);

        // then
        assertThat(event.getAttemptCount()).isEqualTo(10);
        assertThat(event.getStatus()).isEqualTo(OutboxStatus.DEAD);
    }

    @Test
    void publishOne_notDue_doesNotSend() {
        // given
        event.markProcessing("test-worker");
        ReflectionTestUtils.setField(event, "nextAttemptAt", OffsetDateTime.now().plusMinutes(10));
        given(outboxRepository.findById(id)).willReturn(Optional.of(event));

        // when
        worker.publishOne("test-worker",id);

        // then
        then(kafkaTemplate).should(never()).send(anyString(), anyString(), anyString());
        assertThat(event.getStatus()).isEqualTo(OutboxStatus.PROCESSING);
    }
}