package com.mockio.auth_service.config;

/**
 * RestClient 공통 설정 클래스.
 *
 * <p>Apache HttpClient 기반 RequestFactory를 사용하여
 * 연결/응답 타임아웃을 명시적으로 설정한 RestClient Bean을 제공한다.</p>
 *
 * <p>외부 인증 서버(Keycloak 등) 호출 시
 * 무한 대기나 스레드 고갈을 방지하기 위한 보호 설정이다.</p>
 */

import lombok.NonNull;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestClientConfig {

    private final String internalToken;

    public RestClientConfig(@Value("${internal.auth.token}") String internalToken) {
        this.internalToken = internalToken;
    }

    @Bean
    public RestClient userRestClient() {
        HttpComponentsClientHttpRequestFactory requestFactory = getHttpComponentsClientHttpRequestFactory();
        return RestClient.builder()
                .defaultHeader("X-Internal-Token",internalToken)
                .requestFactory(requestFactory)
                .baseUrl("http://localhost:9081")
                .build();
    }

    @Bean
    public RestClient restClient() {
        HttpComponentsClientHttpRequestFactory requestFactory = getHttpComponentsClientHttpRequestFactory();

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    private static @NonNull HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(2))     // 연결 타임아웃
                .setResponseTimeout(Timeout.ofSeconds(2))    // 응답 타임아웃(읽기)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return requestFactory;
    }

}
