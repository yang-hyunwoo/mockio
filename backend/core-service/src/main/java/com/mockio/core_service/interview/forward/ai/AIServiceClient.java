package com.mockio.core_service.interview.forward.ai;

import com.mockio.common_ai_contractor.constant.InterviewErrorCode;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.GeneratedDeepDiveBundle;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.interview.dto.response.SttResponse;
import com.mockio.core_service.user.dto.response.FileUploadResponse;
import com.mockio.core_service.util.APIErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
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
                       r.bodyToMono(APIErrorResponse.class)
                               .map(error -> new CustomApiException(
                                       error.httpCode() != null ? error.httpCode() : r.statusCode().value(),
                                       mapErrorCode(error.errCode()),
                                       error.message() != null ? error.message() : error.errCodeMsg()
                               ))
               )
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
                        r.bodyToMono(APIErrorResponse.class)
                                .map(error -> new CustomApiException(
                                        error.httpCode() != null ? error.httpCode() : r.statusCode().value(),
                                        mapErrorCode(error.errCode()),
                                        error.message() != null ? error.message() : error.errCodeMsg()
                                ))
                )
                .bodyToMono(FollowUpQuestion.class)
                .timeout(Duration.ofSeconds(25))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(2)))
                .block();
    }

    public GeneratedDeepDiveBundle generateDeepDiveResult(DeepDiveCommand req) {
        return aiWebClient.post()
                .uri("/api/ai/v1/questions/deepdive/validate-and-generate")
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
                .bodyToMono(GeneratedDeepDiveBundle.class)
                .timeout(Duration.ofSeconds(25))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(2)))
                .block();
    }



    public SttResponse generateStt(MultipartFile multipartFile) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", multipartFile.getResource())
                    .filename(multipartFile.getOriginalFilename());

            return aiWebClient.post()
                    .uri("/api/ai/v1/answer/stt")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, r ->
                            r.bodyToMono(APIErrorResponse.class)
                                    .map(error -> new CustomApiException(
                                            error.httpCode() != null ? error.httpCode() : r.statusCode().value(),
                                            mapErrorCode(error.errCode()),
                                            error.message() != null ? error.message() : error.errCodeMsg()
                                    ))
                    )
                    .bodyToMono(SttResponse.class)
                    .timeout(Duration.ofSeconds(25))
                    .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                            .filter(this::isRetryable)
                            .maxBackoff(Duration.ofSeconds(2)))
                    .block();
        } catch (CustomApiException e) {
            throw e;

        } catch (Exception e) {
            throw new CustomApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    InterviewErrorCode.AI_SERVICE_FAILED,
                    e.getMessage()
            );
        }
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
