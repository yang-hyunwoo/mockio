package com.mockio.notification_service.kafka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.notification_service.kafka.dto.FeedbackLifecycleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackEventParser {

    private final ObjectMapper objectMapper;

    public FeedbackLifecycleEvent parse(String messageJson) {
        try {
            return objectMapper.readValue(messageJson, FeedbackLifecycleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid event message", e);
        }
    }

    public <T> T payloadAs(FeedbackLifecycleEvent event, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(event.payload(), clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid payload for eventType=" + event.eventType(), e
            );
        }
    }

}
