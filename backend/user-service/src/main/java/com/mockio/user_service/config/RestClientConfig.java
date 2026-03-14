package com.mockio.user_service.config;

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
@EnableConfigurationProperties(InterviewServiceClientProperties.class)
public class RestClientConfig {

    private final String internalToken;
    private final InterviewServiceClientProperties properties;

    public RestClientConfig(
            @Value("${internal.auth.token}") String internalToken,
            InterviewServiceClientProperties properties
    ) {
        this.internalToken = internalToken;
        this.properties = properties;
    }

    @Bean
    public RestClient interviewRestClient(HttpComponentsClientHttpRequestFactory requestFactory) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Internal-Token", internalToken)
                .requestFactory(requestFactory)
                .build();
    }


    @Bean
    public HttpComponentsClientHttpRequestFactory interviewRequestFactory() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(properties.connectTimeoutMs()))
                .setResponseTimeout(Timeout.ofMilliseconds(properties.readTimeoutMs()))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}