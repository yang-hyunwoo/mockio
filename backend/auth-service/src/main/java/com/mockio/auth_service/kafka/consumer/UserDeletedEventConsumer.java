package com.mockio.auth_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.kafka.dto.UserLifecycleEvent;
import com.mockio.auth_service.kafka.OutboxAuthEnqueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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

        enqueueService.enqueueKeycloakDisable(
                event.eventId(),
                keycloakId,
                "USER_DELETED"
        );
    }

    private UserLifecycleEvent parse(String json) {
        try {
            return objectMapper.readValue(json, UserLifecycleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UserDeletedEvent message", e);
        }
    }
}


