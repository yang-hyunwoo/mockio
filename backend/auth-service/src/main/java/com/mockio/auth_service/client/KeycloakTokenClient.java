package com.mockio.auth_service.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.dto.KeycloakErrorResponse;
import com.mockio.auth_service.dto.KeycloakTokenResponse;
import com.mockio.common_spring.exception.KeycloakUnavailableException;
import com.mockio.common_spring.exception.RefreshTokenInvalidException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class KeycloakTokenClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${keycloak.base-url}") private String baseUrl;
    @Value("${keycloak.realm}") private String realm;
    @Value("${keycloak.client-id}") private String clientId;
    @Value("${keycloak.client-secret:}") private String clientSecret;

    @CircuitBreaker(name = "keycloakRefresh", fallbackMethod = "refreshFallback")
    public KeycloakTokenResponse refresh(String refreshToken) {
        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        form.add("refresh_token", refreshToken);

        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        try {
            return restClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (req, res) -> {
                                String raw = readBodySafely(res.getBody());
                                KeycloakErrorResponse err = tryParseError(raw);

                                boolean invalidGrant = raw.contains("\"invalid_grant\"")
                                        || (err != null && "invalid_grant".equals(err.error()));

                                if (invalidGrant) {
                                    // 사용자 토큰 문제: CB 실패로 집계하지 않는 예외로 던짐 (ignoreExceptions)
                                    throw new RefreshTokenInvalidException("Keycloak refresh failed: invalid_grant", null);
                                }

                                // 나머지 4xx도 사용자 문제로 볼지, 시스템 문제로 볼지 정책 결정 필요.
                                // 일반적으로 refresh는 400 계열은 클라이언트 문제로 묶는 경우가 많습니다.
                                if (res.getStatusCode().is4xxClientError()) {
                                    throw new RefreshTokenInvalidException("Keycloak refresh failed: status=" + res.getStatusCode() + ", body=" + raw, new RuntimeException(raw));
                                }

                                // 5xx: Keycloak 시스템 문제 → CB 실패로 집계
                                throw new KeycloakUnavailableException("Keycloak refresh failed: status=" + res.getStatusCode() + ", body=" + raw
                                );
                            })
                    .body(KeycloakTokenResponse.class);

        } catch (ResourceAccessException ex) {
            // 타임아웃/네트워크 계열 (requestFactory timeout 포함) → CB 실패로 집계
            throw new KeycloakUnavailableException("Keycloak refresh failed: network/timeout", ex);
        }
    }

    protected  KeycloakTokenResponse refreshFallback(String refreshToken, Throwable t) {
        // refresh는 대체 성공 응답이 불가능 → 빠르게 장애로 반환
        throw new KeycloakUnavailableException("Keycloak refresh temporarily unavailable (circuit open)", t);
    }

    private String readBodySafely(java.io.InputStream body) {
        try {
            return new String(body.readAllBytes());
        } catch (IOException io) {
            return "<cannot-read-body>";
        }
    }

    private KeycloakErrorResponse tryParseError(String raw) {
        try {
            return objectMapper.readValue(raw, KeycloakErrorResponse.class);
        } catch (Exception ignore) {
            return null;
        }
    }
}