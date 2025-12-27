package com.mockio.user_service.kafka.config;

/**
 * Kafka Consumer 공통 예외 처리 설정
 *
 * - Consumer 처리 중 예외 발생 시 재시도 정책을 정의한다.
 * - 재시도 실패 또는 재시도 자체가 의미 없는 예외는 DLQ(Dead Letter Queue)로 전송한다.
 * - DLQ 토픽은 `{원본 토픽명}.DLQ` 규칙을 따른다.
 *
 * 목적:
 * 1. 일시적인 오류(네트워크, 타이밍 이슈 등)는 재시도로 회복
 * 2. 비즈니스적으로 복구 불가능한 이벤트는 빠르게 격리
 * 3. Consumer 무한 재시도 및 메시지 적체 방지
 */

import com.mockio.common_spring.exception.NonRetryableEventException;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    /**
     * Dead Letter Queue(DLQ)로 메시지를 전송하는 Recoverer Bean
     * @param kafkaTemplate
     * @return
     */
    @Bean
    public DeadLetterPublishingRecoverer dlqRecoverer(KafkaTemplate<Object, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new TopicPartition(
                        record.topic() + ".DLQ",
                        record.partition()
                )
        );
    }

    /**
     * kafka Consumer 에러 처리용 DefaultErrorHandler Bean
     * @param recoverer
     * @return
     */
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(DeadLetterPublishingRecoverer recoverer) {

        // 1초 간격, 3회 재시도
        FixedBackOff backOff = new FixedBackOff(1000L, 3);

        DefaultErrorHandler handler =new DefaultErrorHandler(recoverer, backOff);

        // 재시도 의미 없는 예외
        handler.addNotRetryableExceptions(NonRetryableEventException.class, IllegalArgumentException.class);
        return handler;
    }

}
