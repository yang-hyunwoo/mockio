package com.mockio.auth_service.client;

import com.mockio.auth_service.dto.request.ProfileSyncRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
}
