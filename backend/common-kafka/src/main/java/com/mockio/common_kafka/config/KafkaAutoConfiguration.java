package com.mockio.common_kafka.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

@AutoConfiguration
@EnableKafka
@Import({
        KafkaProducerConfig.class,
        KafkaConsumerConfig.class,
        KafkaErrorHandlerConfig.class
})
public class KafkaAutoConfiguration {
}
