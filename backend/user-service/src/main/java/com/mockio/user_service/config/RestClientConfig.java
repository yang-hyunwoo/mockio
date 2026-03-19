package com.mockio.user_service.config;

import com.mockio.user_service.properties.FileServiceClientProperties;
import com.mockio.user_service.properties.InterviewServiceClientProperties;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
        InterviewServiceClientProperties.class,
        FileServiceClientProperties.class
})
public class RestClientConfig {

    private final String internalToken;

    public RestClientConfig(@Value("${internal.auth.token}") String internalToken) {
        this.internalToken = internalToken;
    }

    @Bean
    public RestClient interviewRestClient(
            HttpComponentsClientHttpRequestFactory requestFactory,
            InterviewServiceClientProperties properties
    ) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Internal-Token", internalToken)
                .requestFactory(requestFactory)
                .build();
    }

    @Bean
    public RestClient fileServiceRestClient(
            HttpComponentsClientHttpRequestFactory requestFactory,
            FileServiceClientProperties properties
    ) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Internal-Token", internalToken)
                .requestFactory(requestFactory)
                .build();
    }
}