package com.mockio.user_service.client;

import com.mockio.user_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.user_service.dto.response.UserInterviewSettingReadResponse;
import lombok.extern.slf4j.Slf4j;
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

    public Void ensureInterviewSetting(Long userId) {
        try {
            return interviewRestClient.post()
                    .uri("/api/interview/v1/internal/interview-setting/ensure")
                    .body(new EnsureInterviewSettingRequest(userId))
                    .retrieve()
                    .body(Void.class);
        } catch (RestClientException ex) {
            log.error("interview-service ensureInterviewSetting 호출 실패. userId={}", userId, ex);
            throw new IllegalStateException("interview-service 호출 실패", ex);
        }
    }

    public UserInterviewSettingReadResponse interviewSetting(Long userId) {
        try {
            return interviewRestClient.get()
                    .uri("/api/interview/v1/internal/setting/{userId}",userId)
                    .retrieve()
                    .body(UserInterviewSettingReadResponse.class);
        } catch (RestClientException ex) {
            log.error("interview-service ensureInterviewSetting 호출 실패. userId={}", userId, ex);
            throw new IllegalStateException("interview-service 호출 실패", ex);
        }
    }

}
