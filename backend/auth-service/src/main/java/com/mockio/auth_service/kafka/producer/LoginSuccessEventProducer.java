package com.mockio.auth_service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.kafka.dto.LoginSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessEventProducer {

    private static final String TOPIC = "user.login.success";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publish(Long userId) {
        try {
            LoginSuccessEvent event = new LoginSuccessEvent(userId, OffsetDateTime.now());
            String payload = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(TOPIC, String.valueOf(userId), payload);
            log.info("published login success event. userId={}", userId);
        } catch (JsonProcessingException e) {
            log.error("failed to serialize login success event. userId={}", userId, e);
        } catch (Exception e) {
            log.error("failed to publish login success event. userId={}", userId, e);
        }
    }

}
