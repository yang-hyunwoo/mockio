package com.mockio.user_service.client;

import com.mockio.user_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.user_service.dto.response.EnsureInterviewSettingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InterviewServiceClient {

    private final RestClient restClient;

    public InterviewServiceClient(
            @Value("${services.interview.base-url") String baseUrl,
            RestClient.Builder builder
    ) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public EnsureInterviewSettingResponse ensureInterviewSetting(String keycloakId) {
        return restClient.post()
                .uri("/api/interview/v1/interview-setting/ensure")
                .body(new EnsureInterviewSettingRequest(keycloakId))
                .retrieve()
                .body(EnsureInterviewSettingResponse.class);
    }
}
