package com.mockio.user_service.kafka.config;

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

    @Bean
    public DeadLetterPublishingRecoverer dlqRecoverer(
            KafkaTemplate<Object, Object> kafkaTemplate) {

        return new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition(
                        record.topic() + ".DLQ",
                        record.partition()
                )
        );
    }

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            DeadLetterPublishingRecoverer recoverer) {

        // 1초 간격, 3회 재시도
        FixedBackOff backOff = new FixedBackOff(1000L, 3);

        DefaultErrorHandler handler =
                new DefaultErrorHandler(recoverer, backOff);

        // 재시도 의미 없는 예외
        handler.addNotRetryableExceptions(
                NonRetryableEventException.class,
                IllegalArgumentException.class
        );

        return handler;
    }
}
