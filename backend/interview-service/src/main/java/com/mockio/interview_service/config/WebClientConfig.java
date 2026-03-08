package com.mockio.interview_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient feedbackWebClient(
            WebClient.Builder builder,
            @Value("${services.feedback.base-url}") String baseUrl
    ) {
        return builder.baseUrl(baseUrl).build();
    }



}
