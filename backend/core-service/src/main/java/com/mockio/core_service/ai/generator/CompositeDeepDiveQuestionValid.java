package com.mockio.core_service.ai.generator;


import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveQuestionValid;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveValid;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.fake.FakeDeepDiveQuestionValid;
import com.mockio.core_service.ai.ollama.generator.OllamaDeepDiveQuestionValid;
import com.mockio.core_service.ai.openAi.generator.OpenAIDeepDiveQuestionValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class CompositeDeepDiveQuestionValid{

    private final OpenAIDeepDiveQuestionValid openAIDeepDiveQuestionValid;
    private final OllamaDeepDiveQuestionValid ollamaDeepDiveQuestionValid;
    private final FakeDeepDiveQuestionValid fakeDeepDiveQuestionValid;


    @Value("${ai.generator}")
    private String mode;

    public DeepDiveValid generateValid(GenerateDeepDiveCommand command) {
        List<DeepDiveQuestionValid> chain = buildChain(mode);

        RuntimeException last = null;
        for (DeepDiveQuestionValid g : chain) {
            try {
                return g.generateValid(command);
            } catch (RuntimeException ex) {
                last = ex;

                log.warn("deep dive valid generation failed. engine={}, fallbackable={}, cause={}",
                        g.engine(), isFallbackable(ex), ex.toString());

                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<DeepDiveQuestionValid> buildChain(String mode) {
        AiEngine primary = parse(mode);

        return switch (primary) {
            case OLLAMA -> List.of(
                    ollamaDeepDiveQuestionValid,
                    openAIDeepDiveQuestionValid,
                    fakeDeepDiveQuestionValid
            );
            case FAKE -> List.of(fakeDeepDiveQuestionValid);
            case OPENAI -> List.of(
                    openAIDeepDiveQuestionValid,
                    ollamaDeepDiveQuestionValid,
                    fakeDeepDiveQuestionValid
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

    private DeepDiveValid fallbackGenerate(GenerateDeepDiveCommand command, Throwable t) {
        log.error("ai deepDive fallback triggered. track={}, difficulty={}, cause={}",
                command.interviewTrack(), command.interviewDifficulty(), t.toString());
        return new DeepDiveValid(
                false,
                "AI 오류",
                "AI 오류"
        );
    }

}
