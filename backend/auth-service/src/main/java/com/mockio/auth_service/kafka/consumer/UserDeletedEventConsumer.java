package com.mockio.auth_service.kafka.consumer;

/**
 * 사용자 삭제(User Deleted) 라이프사이클 이벤트를 처리하는 Kafka 컨슈머.
 *
 * <p>user.lifecycle 토픽에서 USER_DELETED 이벤트를 구독하여,
 * 해당 사용자의 Keycloak 계정 비활성화를 위한 작업을
 * Auth Service Outbox 테이블에 적재한다.</p>
 *
 * <p>이벤트 중복 수신에 대비해 Outbox 기반 멱등성 처리를 수행하며,
 * 실제 Keycloak 호출은 비동기 처리 흐름으로 위임한다.</p>
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.constant.EnqueueResult;
import com.mockio.auth_service.kafka.dto.UserLifecycleEvent;
import com.mockio.auth_service.kafka.OutboxAuthEnqueueService;
import com.mockio.common_core.exception.NonRetryableEventException;
import com.mockio.common_core.exception.TransientBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDeletedEventConsumer {

    private final OutboxAuthEnqueueService enqueueService;

    private final ObjectMapper objectMapper;


    /**
     * Kafka로부터 사용자 라이프사이클 이벤트 메시지를 수신한다.
     *
     * <p>USER_DELETED 이벤트만 처리 대상이며,
     * 그 외 이벤트 타입은 무시한다.</p>
     *
     * @param message Kafka 메시지(JSON 문자열)
     */
    @KafkaListener(topics = "user.lifecycle", groupId = "auth-service")
    public void onMessage(String message, Acknowledgment ack) {
        UserLifecycleEvent event;

        try {
            event = parse(message);
        } catch (Exception e) {
            // 파싱 불가 → 재시도 의미 없음 (ACK 후 종료 또는 DLQ 정책)
            ack.acknowledge();
            throw new com.mockio.common_core.exception.NonRetryableEventException("Invalid message", e);
        }

        // 관심 없는 이벤트는 소비 완료 처리
        if (!"USER_DELETED".equals(event.eventType())) {
            ack.acknowledge();
            return;
        }

        try {
            String keycloakId = event.payload().get("keycloakId").asText();

            EnqueueResult result = enqueueService.enqueueKeycloakDisable(
                    event.eventId(),
                    keycloakId,
                    "USER_DELETED"
            );

            switch (result) {
                case ENQUEUED -> log.info("Outbox enqueued: eventId={}, keycloakId={}", event.eventId(), keycloakId);
                case ALREADY_ENQUEUED -> log.debug("Outbox already enqueued (idempotent): eventId={}, keycloakId={}",
                        event.eventId(), keycloakId);
            }

            // ✅ Outbox enqueue 성공(또는 이미 처리됨) → ACK
            ack.acknowledge();

        } catch (Exception e) {
            throw new NonRetryableEventException("Business error", e);
        }
    }

    /**
     * Kafka 메시지(JSON 문자열)를 UserLifecycleEvent로 역직렬화한다.
     *
     * @param json Kafka 메시지 원문
     * @return 파싱된 UserLifecycleEvent
     * @throws IllegalArgumentException 메시지 형식이 올바르지 않은 경우
     */
    private UserLifecycleEvent parse(String json) {
        try {
            return objectMapper.readValue(json, UserLifecycleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UserDeletedEvent message", e);
        }
    }

}


