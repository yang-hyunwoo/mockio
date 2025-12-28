package com.mockio.auth_service.client;

import com.mockio.auth_service.dto.request.UserUpdateRequest;
import com.mockio.auth_service.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import static org.springframework.http.MediaType.*;

@Component
@RequiredArgsConstructor
public class KeycloakUserClient {

    private final RestClient restClient;
    @Value("${keycloak.base-url}") private String baseUrl;
    @Value("${keycloak.realm}") private String realm;

    @Value("${keycloak.admin.client-id}") private String clientId;
    @Value("${keycloak.admin.client-secret}") private String clientSecret;

    /**
     * Keycloak Admin 토큰
     */
    public String getAdminAccessToken() {
        String tokenUrl = baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        // Keycloak 토큰 응답: access_token, expires_in, token_type ...
        TokenResponse resp = restClient.post()
                .uri(tokenUrl)
                .contentType(APPLICATION_FORM_URLENCODED)
                .body("grant_type=client_credentials"
                        + "&client_id=" + clientId
                        + "&client_secret=" + clientSecret)
                .retrieve()
                .body(TokenResponse.class);

        if (resp == null || resp.accessToken() == null || resp.accessToken().isBlank()) {
            throw new IllegalStateException("Failed to get Keycloak admin access token.");
        }
        return resp.accessToken();
    }

    /**
     * 유저 비활성화(enabled=false)
     */
    @CircuitBreaker(name="keycloakUserDisable", fallbackMethod="disableUserFallback")
    public void disableUser(String keycloakUserId) {
        String adminToken = getAdminAccessToken();

        String url = baseUrl + "/admin/realms/" + realm + "/users/" + keycloakUserId;

        restClient.put()
                .uri(url)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(APPLICATION_JSON)
                .body(new UserUpdateRequest(false)) // enabled=false
                .retrieve()
                .toBodilessEntity();
    }


    public void disableUserFallback(String keycloakUserId, Throwable t) {

        throw new RuntimeException("Keycloak disable failed for userId=" + keycloakUserId, t);
    }
}
