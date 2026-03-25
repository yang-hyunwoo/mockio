package com.mockio.core_service.config;

import com.mockio.core_service.user.properties.SupportServiceClientProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
        SupportServiceClientProperties.class
})
public class RestClientConfig {

    private final String internalToken;

    public RestClientConfig(@Value("${internal.core.token}") String internalToken) {
        this.internalToken = internalToken;
    }

    @Bean
    public RestClient fileServiceRestClient(
            HttpComponentsClientHttpRequestFactory requestFactory,
            SupportServiceClientProperties properties
    ) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Internal-Token", internalToken)
                .requestFactory(requestFactory)
                .build();
    }
}