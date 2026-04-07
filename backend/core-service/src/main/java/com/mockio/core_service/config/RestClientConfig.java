package com.mockio.core_service.config;

import com.mockio.core_service.user.properties.SupportServiceClientProperties;
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

    @Bean
    public RestClient recaptchaRestClient() {
        return RestClient.builder()
                .baseUrl("https://www.google.com")
                .build();
    }

    /**
     * HttpComponents 기반 RequestFactory Bean
     *
     * Apache HttpClient를 사용하여
     * 연결 타임아웃(connect timeout)과 응답 타임아웃(read timeout)을 설정한다.
     * 모든 RestClient 요청에 공통 적용되는 네트워크 설정이다.
     */
    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory(
            @Value("${http.connect-timeout}") int connectTimeout,
            @Value("${http.read-timeout}") int readTimeout
    ) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout))
                .setResponseTimeout(Timeout.ofMilliseconds(readTimeout))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

}