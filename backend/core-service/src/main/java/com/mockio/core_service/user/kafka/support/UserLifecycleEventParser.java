package com.mockio.core_service.user.kafka.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.core_service.user.dto.UserLifecycleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLifecycleEventParser {

    private final ObjectMapper objectMapper;

    public UserLifecycleEvent parse(String messageJson) {
        try {
            return objectMapper.readValue(messageJson, UserLifecycleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid event message", e);
        }
    }

}
