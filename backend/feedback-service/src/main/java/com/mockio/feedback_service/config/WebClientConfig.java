package com.mockio.feedback_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient interviewWebClient(
            WebClient.Builder builder,
            @Value("${services.interview.base-url}") String baseUrl
    ) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean
    WebClient aiWebClient(
            WebClient.Builder builder,
            @Value("${services.ai.base-url}") String baseUrl
    ) {
        return builder.baseUrl(baseUrl).build();
    }

}
