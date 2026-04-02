package com.mockio.common_kafka.config;

/**
 * Kafka Producer 설정 클래스.
 *
 * {KafkaProperties}를 기반으로 Producer 설정을 구성하고,
 * {ProducerFactory} 및 {KafkaTemplate}을 Bean으로 등록한다.
 *
 * 별도의 Producer Bean이 존재하지 않을 경우에만 기본 설정이 적용되며,
 * Key/Value Serializer가 지정되지 않은 경우 기본 Serializer를 설정한다.
 */

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
public class KafkaProducerConfig {

    /**
     * Kafka 메시지 전송을 위한 ProducerFactory를 생성한다.
     *
     * {KafkaProperties} 기반 설정을 사용하며,
     * Serializer가 지정되지 않은 경우 기본 Serializer를 설정한다.
     *
     * @param kafkaProperties Kafka 설정 프로퍼티
     * @return ProducerFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public ProducerFactory<String, String> producerFactory(
            KafkaProperties kafkaProperties
    ) {
        Map<String, Object> props = new HashMap<>();

        props.putAll(kafkaProperties.getProducer().getProperties());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
        props.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Kafka 메시지 전송에 사용되는 KafkaTemplate을 생성한다.
     *
     * @param producerFactory Kafka ProducerFactory
     * @return KafkaTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
