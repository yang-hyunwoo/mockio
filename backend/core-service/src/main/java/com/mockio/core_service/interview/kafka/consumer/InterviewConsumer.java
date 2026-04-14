package com.mockio.core_service.interview.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_core.exception.NonRetryableEventException;
import com.mockio.core_service.interview.kafka.dto.InterviewLifecycleEvent;
import com.mockio.core_service.interview.kafka.dto.request.InterviewCompareQuestionPayload;
import com.mockio.core_service.interview.kafka.support.InterviewEventParser;
import com.mockio.core_service.interview.service.CompareQuestionProcessor;
import com.mockio.core_service.kafka.ProcessedEvent;
import com.mockio.core_service.kafka.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewConsumer {

    private final ProcessedEventRepository processedEventRepository;
    private final InterviewEventParser parser;
    private final CompareQuestionProcessor compareQuestionProcessor;
    private static final String CONSUMER_NAME = "interview-service.compare_question.lifecycle";

    @KafkaListener(topics = "compare_question.lifecycle", groupId = "interview-service")
    public void onMessage(String messageJson, Acknowledgment ack) {
        log.info("kafka message received: {}", messageJson);
        InterviewLifecycleEvent event;

        try {
            event = parser.parse(messageJson);
        } catch (Exception e) {
            //파싱 불가 → 재시도 의미 없음
            throw new NonRetryableEventException("Invalid message", e);
        }

        try {
            switch (event.eventType()) {
                case "compareQuestionSubmitted" -> handleCompareQuestionSubmitted(event);
                default -> throw new NonRetryableEventException(
                        "Unknown eventType=" + event.eventType()
                );
            }
            processedEventRepository.save(ProcessedEvent.of(event.eventId(), CONSUMER_NAME));
            ack.acknowledge();
        } catch (Exception e) {
            // 재시도 가능 → ACK 안 함 → Retry → DLQ
            throw new NonRetryableEventException("Business error", e);
        }
    }

    /**
     * 비교 면접 질문 비동기 생성
     * @param event
     */
    private void handleCompareQuestionSubmitted(InterviewLifecycleEvent event) {
        InterviewCompareQuestionPayload payload = parser.payloadAs(event, InterviewCompareQuestionPayload.class);
        compareQuestionProcessor.process(event.aggregateId(),payload);
    }
}
