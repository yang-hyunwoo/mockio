package com.mockio.auth_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.constant.EnqueueResult;
import com.mockio.auth_service.kafka.dto.UserLifecycleEvent;
import com.mockio.auth_service.kafka.OutboxAuthEnqueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDeletedEventConsumer {

    private final OutboxAuthEnqueueService enqueueService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user.lifecycle", groupId = "auth-service")
    public void onMessage(String message) {
        UserLifecycleEvent event = parse(message);

        if (!"USER_DELETED".equals(event.eventType())) {
            return;
        }

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
    }

    private UserLifecycleEvent parse(String json) {
        try {
            return objectMapper.readValue(json, UserLifecycleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UserDeletedEvent message", e);
        }
    }

}


