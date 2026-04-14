package com.mockio.core_service.interview.forward.ai;

import com.mockio.common_ai_contractor.constant.InterviewErrorCode;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareQuestion;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareQuestionCommand;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummary;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummaryCommand;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveValid;
import com.mockio.common_ai_contractor.generator.deepdive.GeneratedDeepDiveBundle;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowupValid;
import com.mockio.common_ai_contractor.generator.question.GenerateBasicQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GenerateHardQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.interview.dto.response.SttResponse;
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
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class AIServiceClient {

    private final WebClient aiWebClient;

    /**
     * 기본 질문 생성
     * @param req
     * @return
     */
    public GeneratedQuestion generateBasicQuestions(GenerateBasicQuestionCommand req) {
        return post("/api/ai/v1/questions/generate/basic", req, GeneratedQuestion.class);
    }

    /**
     * 심화 질문 생성
     * @param req
     * @return
     */
    public GeneratedQuestion generateHardQuestions(GenerateHardQuestionCommand req) {
        return post("/api/ai/v1/questions/generate/hard", req, GeneratedQuestion.class);
    }

    /**
     * 꼬리 질문 생성
     * @param req
     * @return
     */
    public FollowUpQuestion generateFollowQuestions(FollowUpQuestionCommand req) {
        return post("/api/ai/v1/questions/followup", req, FollowUpQuestion.class);
    }

    /**
     * 꼬리 질문 생성 가능 여부 체크
     * @param req
     * @return
     */
    public FollowupValid generateFollowQuestionsValid(FollowUpQuestionCommand req) {
        return post("/api/ai/v1/questions/followup-valid", req, FollowupValid.class);
    }

    /**
     * 심화 질문 생성 가능 여부 체크
     * @param req
     * @return
     */
    public GeneratedDeepDiveBundle generateDeepDiveResult(DeepDiveCommand req) {
        return post("/api/ai/v1/questions/deepdive/validate-and-generate", req, GeneratedDeepDiveBundle.class);
    }

    /**
     * 심화 질문 생성
     * @param req
     * @return
     */
    public DeepDiveValid generateDeepDiveValid(DeepDiveCommand req) {
       return post("/api/ai/v1/questions/deepdive/deep-dive-valid", req, DeepDiveValid.class);
    }

    /**
     * 면접 비교 생성
     * @param req
     */
    public GeneratedCompareSummary generateCompareInterview(GeneratedCompareSummaryCommand req) {
        return post("/api/ai/v1/compare/summary/generate", req, GeneratedCompareSummary.class);
    }

    /**
     * 이전 면접 질문 비교 생성
     * @param req
     * @return
     */
    public GeneratedCompareQuestion generateCompareQuestion(GeneratedCompareQuestionCommand req) {
        return post("/api/ai/v1/compare/question/generate", req, GeneratedCompareQuestion.class);
    }

    /**
     * stt 변환
     * @param multipartFile
     * @return
     */
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

    /**
     * WebClient Post 전송
     * @param uri
     * @param body
     * @param responseType
     * @return
     * @param <T>
     */
    public <T> T post(String uri, Object body, Class<T> responseType) {
        return aiWebClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(APIErrorResponse.class)
                                .map(error -> new CustomApiException(
                                        error.httpCode() != null ? error.httpCode() : r.statusCode().value(),
                                        mapErrorCode(error.errCode()),
                                        error.message() != null ? error.message() : error.errCodeMsg()
                                ))
                )
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(25))
                .retryWhen(Retry.backoff(1, Duration.ofMillis(300))
                        .filter(this::isRetryable)
                        .maxBackoff(Duration.ofSeconds(2)))
                .block();
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

    private boolean isRetryable(Throwable t) {
        return t instanceof TimeoutException || t.getCause() instanceof IOException;
    }

}
