package com.mockio.auth_service.config;

/**
 * Redis 설정 클래스
 *
 * Redis 연결(ConnectionFactory) 및 RedisTemplate 직렬화 전략을 정의한다.
 * Lettuce 클라이언트를 기반으로 단일 Redis 서버에 연결하며,
 * Key는 문자열, Value는 JSON 형태로 직렬화하여 저장한다.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * Redis 연결 팩토리 생성
     *
     * application.yml 또는 properties에 정의된 host, port 정보를 기반으로
     * 단일 Redis 서버에 연결하는 LettuceConnectionFactory를 생성한다.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port
    ) {
        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(config);
    }

    /**
     * RedisTemplate 설정
     *
     * Key는 String 직렬화, Value는 JSON 직렬화를 사용하여
     * 객체 데이터를 Redis에 저장할 수 있도록 구성한다.
     * Hash 자료구조에도 동일한 직렬화 전략을 적용한다.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // key 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // value 직렬화 (JSON)
        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(Object.class);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * StringRedisTemplate 설정
     *
     * Key와 Value를 모두 String으로 처리하는 템플릿으로,
     * 단순 문자열 기반 데이터 저장 및 조회에 사용된다.
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        return new StringRedisTemplate(connectionFactory);
    }

}
