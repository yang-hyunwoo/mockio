package com.mockio.core_service.user.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_core.exception.NonRetryableEventException;
import com.mockio.core_service.internalmapper.InternalMapper;
import com.mockio.core_service.interview.dto.request.InternalEnsureInterviewSettingRequest;
import com.mockio.core_service.interview.service.UserInterviewSettingService;
import com.mockio.core_service.kafka.ProcessedEvent;
import com.mockio.core_service.kafka.ProcessedEventRepository;
import com.mockio.core_service.user.dto.request.EnsureInterviewSettingRequest;
import com.mockio.core_service.user.kafka.dto.LoginSuccessEvent;
import com.mockio.core_service.user.kafka.dto.UserLifecycleEvent;
import com.mockio.core_service.user.kafka.support.UserEventParser;
import com.mockio.core_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final UserEventParser parser;
    private final ProcessedEventRepository processedEventRepository;
    private static final String CONSUMER_NAME = "core-service.user-lifecycle";
    private final UserInterviewSettingService userInterviewSettingService;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    //회원 가입 후 유저 면접 설정
    @KafkaListener(topics = "user.lifecycle" , groupId = "core-service")
    public void onMessage(String messageJson , Acknowledgment ack) {
        log.info("kafka message received: {}", messageJson);
        UserLifecycleEvent event;

        try {
            event = parser.parse(messageJson);
        } catch (Exception e) {
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
                case "signupAfterInterviewSetting" -> handleSignupInterviewSetting(event);
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

    //로그인 후 유저 실패 카운트 , 접속일자 수정
    @KafkaListener(topics = "user.login.success", groupId = "core-service-login-success")
    public void consume(String message) {
        try {
            LoginSuccessEvent event = objectMapper.readValue(message, LoginSuccessEvent.class);
            userService.resetFailCount(event.userId());
            log.info("consumed login success event. userId={}", event.userId());
        } catch (Exception e) {
            log.error("failed to consume login success event. message={}", message, e);
            throw new RuntimeException(e);
        }
    }


    private void handleSignupInterviewSetting(UserLifecycleEvent event) {
        InternalEnsureInterviewSettingRequest internalEnsureInterviewSettingRequest = parser.payloadAs(event, InternalEnsureInterviewSettingRequest.class);
        userInterviewSettingService.ensureInterviewSettingSave(InternalMapper.toInternalEnsureInterviewSetting(new EnsureInterviewSettingRequest(internalEnsureInterviewSettingRequest.userId())));

    }
}
