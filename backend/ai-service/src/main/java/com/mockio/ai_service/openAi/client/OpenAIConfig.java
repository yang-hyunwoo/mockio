package com.mockio.ai_service.openAi.client;

/**
 * OpenAI API 호출을 위한 WebClient 설정 클래스.
 *
 * <p>OpenAI 기본 엔드포인트와 인증 헤더(Bearer Token)를 포함한
 * WebClient Bean을 생성하여, OpenAIClient에서 재사용하도록 제공한다.</p>
 *
 * <p>API Key는 외부 설정(application.yml 등)을 통해 주입되며,
 * 소스 코드에는 직접 포함되지 않는다.</p>
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {

    @Bean
    WebClient openAiWebClient(@Value("${spring.ai.openai.api-key}") String apiKey) {
        return WebClient.builder()
                .baseUrl("https://api.openai.com")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
