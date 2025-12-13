package com.mockio.auth_service.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.auth_service.dto.KeycloakErrorResponse;
import com.mockio.auth_service.dto.KeycloakTokenResponse;
import com.mockio.common_spring.exception.RefreshTokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    public KeycloakTokenResponse refresh(String refreshToken) {
        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        form.add("refresh_token", refreshToken);

        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        return restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        (req, res) -> {
                            String raw;
                            try {
                                raw = new String(res.getBody().readAllBytes());
                            } catch (IOException io) {
                                throw new RefreshTokenInvalidException("Keycloak refresh failed (cannot read body)", io);
                            }

                            KeycloakErrorResponse err = null;
                            try {
                                err = objectMapper.readValue(raw, KeycloakErrorResponse.class);
                            } catch (Exception ignore) {
                                // raw로만 판단
                            }

                            if (raw.contains("\"invalid_grant\"")
                                    || (err != null && "invalid_grant".equals(err.error()))) {
                                throw new RefreshTokenInvalidException("Keycloak refresh failed: invalid_grant",null);
                            }
                            throw new RefreshTokenInvalidException("Keycloak refresh failed: status=" + res.getStatusCode() + ", body=" + raw,new RuntimeException(raw));
                        })
                .body(KeycloakTokenResponse.class);
    }
}