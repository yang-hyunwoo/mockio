package com.mockio.core_service.feedback.client;

import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedbackCommand;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.util.APIErrorResponse;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static com.mockio.common_core.constant.CommonErrorEnum.ILLEGALSTATE;

@Service
@RequiredArgsConstructor
public class AiFeedbackClient {

    private final WebClient aiWebClient;

    public GeneratedFeedback generateQuestionFeedback(GenerateFeedbackCommand req) {
        return aiWebClient.post()
                .uri("/api/ai/v1/feedback/question")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(APIErrorResponse.class)
                                .map(error -> new CustomApiException(
                                        error.httpCode() != null ? error.httpCode() : r.statusCode().value(),
                                        mapErrorCode(error.errCode()),
                                        error.message() != null ? error.message() : error.errCodeMsg()
                                ))
                )
                .bodyToMono(GeneratedFeedback.class)
                .timeout(Duration.ofSeconds(25))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(2)))
                .block();
    }

    public GeneratedSummaryFeedback generateSummaryFeedback(GeneratedSummaryFeedbackCommand req) {
        return aiWebClient.post()
                .uri("/api/ai/v1/feedback/summary")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(APIErrorResponse.class)
                                .map(error -> new CustomApiException(
                                        error.httpCode() != null ? error.httpCode() : r.statusCode().value(),
                                        mapErrorCode(error.errCode()),
                                        error.message() != null ? error.message() : error.errCodeMsg()
                                ))
                )
                .bodyToMono(GeneratedSummaryFeedback.class)
                .timeout(Duration.ofSeconds(25))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(2)))
                .block();
    }

    private boolean isRetryable(Throwable t) {
        return t instanceof TimeoutException
                || t instanceof IOException
                || t instanceof WebClientRequestException;
    }

    private AIErrorCodeEnum mapErrorCode(String errCode) {
        if (errCode == null || errCode.isBlank()) {
            return AIErrorCodeEnum.ILLEGAL_STATE;
        }
        try {
            return AIErrorCodeEnum.valueOf(errCode);
        } catch (IllegalArgumentException e) {
            return AIErrorCodeEnum.ILLEGAL_STATE;
        }
    }

}
