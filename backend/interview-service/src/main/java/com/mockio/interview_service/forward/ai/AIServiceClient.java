package com.mockio.interview_service.forward.ai;

import com.mockio.common_ai_contractor.constant.InterviewErrorCode;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_spring.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class AIServiceClient {

    private final WebClient aiWebClient;

    public GeneratedQuestion generateQuestions(GenerateQuestionCommand req) {
       return aiWebClient.post()
                .uri("/api/ai/v1/questions/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).map(
                                msg -> new CustomApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), InterviewErrorCode.QUESTIONS_ALREADY_GENERATED, msg)
                        ))
                .bodyToMono(GeneratedQuestion.class)
                .timeout(Duration.ofSeconds(25))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(2)))
                .block();
    }

    private boolean isRetryable(Throwable t) {
        return t instanceof TimeoutException || t.getCause() instanceof IOException;
    }

    public FollowUpQuestion generateFollowQuestions(FollowUpQuestionCommand req) {
        return aiWebClient.post()
                .uri("/api/ai/v1/questions/followup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).map(
                                msg -> new CustomApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), InterviewErrorCode.QUESTIONS_ALREADY_GENERATED, msg)
                        ))
                .bodyToMono(FollowUpQuestion.class)
                .timeout(Duration.ofSeconds(25))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(2)))
                .block();
    }
}
