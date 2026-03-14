package com.mockio.auth_service.client;

import com.mockio.auth_service.dto.request.ProfileSyncRequest;
import com.mockio.auth_service.dto.response.UserIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileClient {

    private final RestClient userRestClient;

    public void syncProfile(ProfileSyncRequest request) {
        userRestClient.post()
                .uri("/api/users/v1/internal/me/sync")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public UserIdResponse getUserId(String keycloakUserId) {
        try {
            UserIdResponse response  = userRestClient.get()
                    .uri("/api/users/v1/internal/by-keycloak-id/{keycloakUserId}", keycloakUserId)
                    .retrieve()
                    .body(UserIdResponse.class);
            if (response == null) {
                log.error("user-service 응답 본문이 null 입니다. keycloakUserId={}", keycloakUserId);
                throw new IllegalStateException("user-service 응답이 비어 있습니다.");
            }
            return response;
        } catch (RestClientException ex) {
            log.error("user-service userId 조회 실패. keycloakUserId={}", keycloakUserId, ex);
            throw new IllegalStateException("user-service 호출 실패", ex);
        }
    }

}
