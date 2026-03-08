package com.mockio.interview_service.config;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.interview_service.dto.response.FeedbackDetailResponse;
import com.mockio.interview_service.kafka.dto.response.InterviewAnswerDetailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.mockio.common_core.constant.CommonErrorEnum.ILLEGALSTATE;

@Service
public class FeedbackServiceClient {

    private final WebClient feedbackWebClient;
    private final String internalToken;

    public FeedbackServiceClient(WebClient feedbackWebClient,
                                 @Value("${internal.auth.token}") String internalToken) {
        this.feedbackWebClient = feedbackWebClient;
        this.internalToken = internalToken;
    }

    /**
     * 질문별 피드백
     * @param answerId
     * @return
     */
    public FeedbackDetailResponse getFeedbackDetail(Long answerId) {
        return feedbackWebClient.get()
                .uri("/api/feedback/v1/internal/{answerId}", answerId)
                .header("X-Internal-Token", internalToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .map(msg -> new CustomApiException(
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        ILLEGALSTATE,
                                        "feedback-service error: " + msg
                                ))
                )
                .bodyToMono(FeedbackDetailResponse.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(200))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(1)))
                .block();
    }



    private boolean isRetryable(Throwable t) {
        // 네트워크/타임아웃/일시적 장애만 재시도
        return t instanceof TimeoutException
                || t instanceof IOException
                || t instanceof WebClientRequestException;
    }

}
