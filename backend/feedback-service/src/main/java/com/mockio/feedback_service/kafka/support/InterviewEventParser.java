package com.mockio.feedback_service.kafka.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.feedback_service.kafka.dto.InterviewLifecycleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewEventParser {

    private final ObjectMapper objectMapper;

    public InterviewLifecycleEvent parse(String messageJson) {
        try {
            return objectMapper.readValue(messageJson, InterviewLifecycleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid event message", e);
        }
    }

    public <T> T payloadAs(InterviewLifecycleEvent event, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(event.payload(), clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid payload for eventType=" + event.eventType(), e
            );
        }
    }

}
