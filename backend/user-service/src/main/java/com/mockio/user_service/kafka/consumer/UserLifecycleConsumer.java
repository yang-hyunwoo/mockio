package com.mockio.user_service.kafka.consumer;

import com.mockio.common_core.exception.NonRetryableEventException;
import com.mockio.user_service.kafka.domain.ProcessedEvent;
import com.mockio.user_service.dto.UserLifecycleEvent;
import com.mockio.user_service.kafka.repository.ProcessedEventRepository;
import com.mockio.user_service.kafka.support.UserLifecycleEventParser;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLifecycleConsumer {

    private final ProcessedEventRepository processedEventRepository;
    private final UserLifecycleEventParser parser;
    private static final String CONSUMER_NAME = "user-service.user-lifecycle";

    @KafkaListener(topics = "user.lifecycle", groupId = "user-service")
    @Transactional
    public void onMessage(String messageJson, Acknowledgment ack) {

        UserLifecycleEvent event;
        try {
            event = parser.parse(messageJson);
        } catch (Exception e) {
            //파싱 불가 → 재시도 의미 없음
            throw new NonRetryableEventException("Invalid message", e);
        }

        try {
            processedEventRepository.save(ProcessedEvent.of(event.eventId(), CONSUMER_NAME));
        } catch (DataIntegrityViolationException e) {
            // 이미 처리됨 → 정상 종료(ACK)
            ack.acknowledge();
            return;
        }

        try {
            handleBusiness(event);
            ack.acknowledge();
        } catch (Exception e) {
            //재시도 의미 없음
            throw new NonRetryableEventException("Business error", e);
        }
    }

    private void handleBusiness(UserLifecycleEvent event) {
        switch (event.eventType()) {
            case "USER_DELETED" -> handleUserDeleted(event);
            default -> throw new NonRetryableEventException("Unknown eventType=" + event.eventType());
        }
    }

    private void handleUserDeleted(UserLifecycleEvent event) {
        Long userId = event.aggregateId();
    }

    private void handleUserSuspended(UserLifecycleEvent event) {
    }

}
