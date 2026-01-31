package com.mockio.feedback_service.config;

import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.feedback_service.kafka.dto.response.InterviewAnswerDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static com.mockio.common_spring.constant.CommonErrorEnum.ILLEGALSTATE;

@Service
@RequiredArgsConstructor
public class InterviewServiceClient {

    private final WebClient interviewWebClient;

    /**
     * 질문별 피드백
     * @param answerId
     * @return
     */
    public InterviewAnswerDetailResponse getAnswerDetail(Long answerId) {
        return interviewWebClient.get()
                .uri("/internal/interview-answers/{answerId}", answerId)
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

//    public InterviewAnswerListResponse getInterviewAnswers(Long interviewId) {
//        return interviewRestClient.get()
//                .uri("/internal/interviews/{interviewId}/answers", interviewId)
//                .retrieve()
//                .body(InterviewAnswerListResponse.class);
//    }

    private boolean isRetryable(Throwable t) {
        // 네트워크/타임아웃/일시적 장애만 재시도
        return t instanceof TimeoutException
                || t instanceof IOException
                || t instanceof WebClientRequestException;
    }
}
