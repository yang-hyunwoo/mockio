package com.mockio.common_kafka.config;

/**
 * Kafka Consumer ListenerContainerFactory 설정 클래스.
 *
 * {ConcurrentKafkaListenerContainerFactory}를 구성하여
 * Kafka 메시지 소비 시 사용할 ConsumerFactory, 에러 처리 전략, Ack 방식을 설정한다.
 *
 * {DefaultErrorHandler}를 통해 메시지 처리 실패 시 재시도 및 예외 처리 정책을 적용하며,
 * AckMode를 {MANUAL}로 설정하여 메시지 커밋 시점을 직접 제어한다.
 *
 * {kafkaListenerContainerFactory} Bean이 존재하지 않을 경우에만 등록된다.
 */

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;

@AutoConfiguration
public class KafkaConsumerConfig {

    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler kafkaErrorHandler
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }

}
