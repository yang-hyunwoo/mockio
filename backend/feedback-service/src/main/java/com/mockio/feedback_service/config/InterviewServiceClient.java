package com.mockio.feedback_service.config;

import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.feedback_service.kafka.dto.response.InterviewAnswerDetailResponse;
import lombok.RequiredArgsConstructor;
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
import org.springframework.beans.factory.annotation.Value;

import static com.mockio.common_spring.constant.CommonErrorEnum.ILLEGALSTATE;

@Service
public class InterviewServiceClient {

    private final WebClient interviewWebClient;
    private final String internalToken;

    public InterviewServiceClient(WebClient interviewWebClient,
                                  @Value("${internal.auth.token}") String internalToken) {
        this.interviewWebClient = interviewWebClient;
        this.internalToken = internalToken;
    }


    /**
     * 질문별 피드백
     * @param answerId
     * @return
     */
    public InterviewAnswerDetailResponse getAnswerDetail(Long answerId) {
        return interviewWebClient.get()
                .uri("/api/interview/v1/internal/interview-answers/{answerId}", answerId)
                .header("X-Internal-Token", internalToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .map(msg -> new CustomApiException(
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        ILLEGALSTATE,
                                        "interview-service error: " + msg
                                ))
                )
                .bodyToMono(InterviewAnswerDetailResponse.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(200))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(1)))
                .block();
    }

    /**
     * 전체 피드백
     * @param interviewId
     * @return
     */
    public List<InterviewAnswerDetailResponse> getInterviewList(Long interviewId) {
        return interviewWebClient.get()
                .uri("/api/interview/v1/internal/interview-all/{interviewId}", interviewId)
                .header("X-Internal-Token", internalToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .map(msg -> new CustomApiException(
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        ILLEGALSTATE,
                                        "interview-service error: " + msg
                                ))
                )
                .bodyToMono(new ParameterizedTypeReference<List<InterviewAnswerDetailResponse>>() {})
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
