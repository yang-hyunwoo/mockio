package com.mockio.auth_service.config;

/**
 * RestClient 공통 설정 클래스.
 *
 * Apache HttpClient 기반 RequestFactory를 사용하여
 * 연결/응답 타임아웃을 명시적으로 설정한 RestClient Bean을 제공한다.
 *
 * 무한 대기나 스레드 고갈을 방지하기 위한 보호 설정이다.</p>
 */

import com.mockio.auth_service.properties.UserServiceClientProperties;
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
@EnableConfigurationProperties(UserServiceClientProperties.class)
public class RestClientConfig {

    private final String internalToken;
    private final UserServiceClientProperties properties;

    public RestClientConfig(
            @Value("${internal.auth.token}") String internalToken,
            UserServiceClientProperties properties
    ) {
        this.internalToken = internalToken;
        this.properties = properties;
    }

    /**
     * User 서비스 호출용 RestClient Bean
     *
     * baseUrl과 내부 인증 토큰 헤더(X-Internal-Token)를 기본 설정으로 포함하며,
     * 커스텀 RequestFactory를 통해 타임아웃 정책이 적용된다.
     */
    @Bean
    public RestClient userRestClient(HttpComponentsClientHttpRequestFactory requestFactory) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Internal-Token", internalToken)
                .requestFactory(requestFactory)
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
    public HttpComponentsClientHttpRequestFactory requestFactory() {
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
