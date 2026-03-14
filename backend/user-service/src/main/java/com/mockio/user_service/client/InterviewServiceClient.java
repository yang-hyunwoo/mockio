package com.mockio.user_service.client;

import com.mockio.user_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.user_service.dto.response.EnsureInterviewSettingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class InterviewServiceClient {

    private final RestClient interviewRestClient;

    public InterviewServiceClient(RestClient interviewRestClient) {
        this.interviewRestClient = interviewRestClient;
    }

    public EnsureInterviewSettingResponse ensureInterviewSetting(Long userId) {
        try {
            return interviewRestClient.post()
                    .uri("/api/interview/v1/internal/interview-setting/ensure")
                    .body(new EnsureInterviewSettingRequest(userId))
                    .retrieve()
                    .body(EnsureInterviewSettingResponse.class);
        } catch (RestClientException ex) {
            log.error("interview-service ensureInterviewSetting 호출 실패. keycloakId={}", userId, ex);
            throw new IllegalStateException("interview-service 호출 실패", ex);
        }
    }

}
