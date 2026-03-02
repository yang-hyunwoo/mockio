package com.mockio.user_service.client;

import com.mockio.user_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.user_service.dto.response.EnsureInterviewSettingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InterviewServiceClient {

    private final RestClient restClient;
    private final String internalToken;

    public InterviewServiceClient(
            @Value("${services.interview.base-url}") String baseUrl,
            RestClient.Builder builder,
            @Value("${internal.auth.token}") String internalToken
    ) {
        this.restClient = builder.baseUrl(baseUrl).build();
        this.internalToken = internalToken;
    }

    public EnsureInterviewSettingResponse ensureInterviewSetting(String keycloakId) {
        return restClient.post()
                .uri("/api/interview/v1/internal/interview-setting/ensure")
                .header("X-Internal-Token",internalToken)
                .body(new EnsureInterviewSettingRequest(keycloakId))
                .retrieve()
                .body(EnsureInterviewSettingResponse.class);
    }

    private String currentBearerToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        return null;
    }

}
