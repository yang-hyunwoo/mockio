package com.mockio.notification_service.kafka.consumer;

import com.mockio.common_core.exception.NonRetryableEventException;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.notification_service.domain.ProcessedEvent;
import com.mockio.notification_service.kafka.dto.FeedbackLifecycleEvent;
import com.mockio.notification_service.kafka.dto.SummaryFeedbackCompletedNotificationPayload;
import com.mockio.notification_service.kafka.repository.ProcessedEventRepository;
import com.mockio.notification_service.kafka.util.FeedbackEventParser;
import com.mockio.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackConsumer {

    private final ProcessedEventRepository processedEventRepository;
    private final FeedbackEventParser parser;
    private final NotificationService notificationService;
    private static final String CONSUMER_NAME = "feedback-service.feedback.lifecycle";

    @KafkaListener(topics = "feedback.lifecycle", groupId = "notification-service")
    public void onMessage(String messageJson, Acknowledgment ack) {
        log.info("kafka message received: {}", messageJson);
        FeedbackLifecycleEvent event;

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
            switch (event.eventType()) {
                case "SummaryFeedbackCompleted" -> handleFeedbackCompleted(event);
                default -> throw new NonRetryableEventException(
                        "Unknown eventType=" + event.eventType()
                );
            }
            ack.acknowledge();
        } catch (Exception e) {
            // 재시도 가능 → ACK 안 함 → Retry → DLQ
            throw new NonRetryableEventException("Business error", e);
        }
    }

    private void handleFeedbackCompleted(FeedbackLifecycleEvent event) {

        SummaryFeedbackCompletedNotificationPayload payload = parser.payloadAs( event, SummaryFeedbackCompletedNotificationPayload.class);
        notificationService.createSummaryFeedbackReadyNotification(event.eventId(),payload);
    }

}
