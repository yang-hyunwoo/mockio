package com.mockio.core_service.user.kafka.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.core_service.user.kafka.dto.UserLifecycleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventParser {

    private final ObjectMapper objectMapper;

    public UserLifecycleEvent parse(String messageJson) {
        try {
            return objectMapper.readValue(messageJson, UserLifecycleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid event message", e);
        }
    }

    public <T> T payloadAs(UserLifecycleEvent event, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(event.payload(), clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid payload for eventType=" + event.eventType(), e
            );
        }
    }

}
