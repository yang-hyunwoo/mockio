package com.mockio.core_service.ai.generator;

/**
 * 인터뷰 질문 생성을 위한 합성(Composite) 질문 생성기.
 *
 * <p>설정 값(ai.generator)에 따라 OpenAI, Ollama 구현체 중
 * 하나를 선택하여 질문을 생성하며, 기본 전략은 OpenAI 사용 후
 * 실패 시 Ollama로 폴백하는 구조를 따른다.</p>
 *
 * <p>외부 AI API 장애, 레이트 리밋(429) 등 불안정한 상황에서도
 * 서비스 연속성을 보장하기 위한 책임을 가진다.</p>
 */

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.*;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.fake.FakeFeedBackEvaluationGenerator;
import com.mockio.core_service.ai.ollama.generator.OllamaFeedbackEvaluationGenerator;
import com.mockio.core_service.ai.openAi.generator.OpenAIFeedbackEvaluationGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompositeFeedbackEvaluationGenerator{

    private final OpenAIFeedbackEvaluationGenerator openAIFeedbackEvaluationGenerator;
    private final OllamaFeedbackEvaluationGenerator ollamaFeedbackEvaluationGenerator;
    private final FakeFeedBackEvaluationGenerator fakeFeedBackEvaluationGenerator;

    @Value("${ai.generator}")
    private String mode;

    public GeneratedFeedbackEvaluation generate(GenerateFeedbackCommand command) {

        List<FeedbackEvaluationGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (FeedbackEvaluationGenerator g : chain) {
            try {
                return g.generate(command);
            } catch (RuntimeException ex) {
                log.error("generator failed. engine={}, message={}", g.engine(), ex.getMessage(), ex);

                last = ex;
                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<FeedbackEvaluationGenerator> buildChain(String mode) {
        AiEngine primary = parse(mode);

        return switch (primary) {
            case OLLAMA -> List.of(
                    ollamaFeedbackEvaluationGenerator,
                    openAIFeedbackEvaluationGenerator,
                    fakeFeedBackEvaluationGenerator
            );
            case FAKE -> List.of(fakeFeedBackEvaluationGenerator);
            case OPENAI -> List.of(
                    openAIFeedbackEvaluationGenerator,
                    ollamaFeedbackEvaluationGenerator,
                    fakeFeedBackEvaluationGenerator
            );
        };
    }

    private AiEngine parse(String mode) {
        if ("ollama".equalsIgnoreCase(mode)) {
            return AiEngine.OLLAMA;
        }
        if ("fake".equalsIgnoreCase(mode)) {
            return AiEngine.FAKE;
        }
        return AiEngine.OPENAI;
    }

    private boolean isFallbackable(Throwable ex) {
        if (ex instanceof CustomApiException cae) {
            return cae.getErrorEnum() == AIErrorCodeEnum.RATE_LIMIT
                    || cae.getErrorEnum() == AIErrorCodeEnum.TEMPORARY_ERROR;
        }
        return true;
    }

    private GeneratedFeedbackEvaluation fallbackGenerate(GenerateFeedbackCommand command, Throwable ex) {
        log.error("ai feedback fallback triggered. track={}, difficulty={}, cause={}",
                command.track(), command.difficulty(), ex.toString());

        // 프로젝트 정책에 맞게 최소 fallback 구성
        return new GeneratedFeedbackEvaluation(
                0,
                new FeedbackDimensions(
                        0,
                        0,
                        0
                ),
                new FeedbackJobMetric(
                        0,
                        0,
                        0
                ),
                "EMPTY",
                "잘못된 정보 입니다.",
                "잘못된 요약 입니다.",
                List.of()
        );
    }

}
