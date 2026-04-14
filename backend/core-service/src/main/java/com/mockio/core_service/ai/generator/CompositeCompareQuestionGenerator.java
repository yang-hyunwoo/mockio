package com.mockio.core_service.ai.generator;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.compare.*;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.fake.FakeCompareQuestionGenerator;
import com.mockio.core_service.ai.ollama.generator.OllamaCompareQuestionGenerator;
import com.mockio.core_service.ai.openAi.generator.OpenAICompareQuestionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompositeCompareQuestionGenerator {

    private final OpenAICompareQuestionGenerator openAICompareQuestionGenerator;
    private final OllamaCompareQuestionGenerator ollamaCompareQuestionGenerator;
    private final FakeCompareQuestionGenerator fakeCompareQuestionGenerator;

    @Value("${ai.generator}")
    private String mode;

    public GeneratedCompareQuestion generate(GeneratedCompareQuestionCommand command) {
        List<CompareQuestionGenerator> chain = buildChain(mode);

        RuntimeException last = null;

        for (CompareQuestionGenerator generator : chain) {
            try {
                return generator.generate(command);
            } catch (RuntimeException ex) {
                last = ex;

                log.warn("compare summary generation failed. engine={}, fallbackable={}, cause={}",
                        generator.engine(), isFallbackable(ex), ex.toString());

                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<CompareQuestionGenerator> buildChain(String mode) {
        AiEngine primary = parse(mode);

        return switch (primary) {
            case OLLAMA -> List.of(
                    ollamaCompareQuestionGenerator,
                    openAICompareQuestionGenerator,
                    fakeCompareQuestionGenerator
            );
            case FAKE -> List.of(fakeCompareQuestionGenerator);
            case OPENAI -> List.of(
                    openAICompareQuestionGenerator,
                    ollamaCompareQuestionGenerator,
                    fakeCompareQuestionGenerator
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

    private GeneratedCompareQuestion fallbackGenerate(GeneratedCompareQuestionCommand command, Throwable ex) {

        log.warn("ai compare question fallback triggered. cause={}",
                ex == null ? "unknown" : ex.toString());

        return new GeneratedCompareQuestion(
                "비교 요약 생성에 실패했습니다.",
                "현재 AI 응답 생성이 불가능하여 기본 결과를 반환합니다.",
                List.of("이전 기록이 누적되고 있습니다."),
                List.of("비교 분석을 다시 시도해 주세요."),
                List.of("성장 포인트를 재분석할 필요가 있습니다."),
                "BETTER",
                "fallback",
                "fallback",
                "v0.0",
                0.0
        );
    }


}