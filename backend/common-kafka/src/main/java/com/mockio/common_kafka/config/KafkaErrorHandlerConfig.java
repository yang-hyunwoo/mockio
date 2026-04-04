package com.mockio.common_kafka.config;

/**
 * Kafka Consumer 에러 처리 및 DLQ(Dead Letter Queue) 전송 설정 클래스.
 *
 * 메시지 처리 실패 시 {DefaultErrorHandler}를 통해 재시도 정책을 적용하고,
 * 재시도 횟수 초과 또는 복구 불가능한 예외 발생 시
 * {DeadLetterPublishingRecoverer}를 사용하여 DLQ 토픽으로 메시지를 전송한다.
 *
 * 또한 DLQ 전송 전용 {ProducerFactory} 및 {KafkaTemplate}을 구성하여
 * 다양한 payload 타입을 안전하게 직렬화할 수 있도록 지원한다.
 */

import com.mockio.common_core.exception.NonRetryableEventException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@RequiredArgsConstructor
public class KafkaErrorHandlerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    @ConditionalOnMissingBean(name = "deadLetterProducerFactory")
    public ProducerFactory<String, Object> deadLetterProducerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.putAll(kafkaProperties.getProducer().getProperties());

        return new DefaultKafkaProducerFactory<>(
                props,
                new StringSerializer(),
                new DelegatingByTypeSerializer(Map.of(
                        byte[].class, new ByteArraySerializer(),
                        String.class, new StringSerializer(),
                        Object.class, new JsonSerializer<>()
                ))
        );
    }

    @Bean
    @ConditionalOnMissingBean(name = "deadLetterKafkaTemplate")
    public KafkaTemplate<String, Object> deadLetterKafkaTemplate(
            ProducerFactory<String, Object> deadLetterProducerFactory
    ) {
        return new KafkaTemplate<>(deadLetterProducerFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
            KafkaTemplate<String, Object> deadLetterKafkaTemplate
    ) {
        return new DeadLetterPublishingRecoverer(
                deadLetterKafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + ".DLQ", record.partition())
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultErrorHandler kafkaErrorHandler(DeadLetterPublishingRecoverer recoverer) {
        var backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10000L);

        var handler = new DefaultErrorHandler(recoverer, backOff);
        handler.addNotRetryableExceptions(NonRetryableEventException.class);
        return handler;
    }

}