package com.mockio.user_service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLifecycleDlqConsumer {

    @KafkaListener(topics = "user.lifecycle.DLQ", groupId = "user-service-dlq")
    public void onDlq(String message, Acknowledgment ack) {
        // 최소: 에러 로그
        log.error("DLQ received: {}", message);

        // 확장: Slack/Email, 혹은 dead-letter 테이블 저장
    }

}