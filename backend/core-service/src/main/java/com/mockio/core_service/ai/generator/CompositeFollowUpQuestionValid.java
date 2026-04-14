package com.mockio.core_service.ai.generator;



import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.*;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.fake.FakeFollowUpQuestionValid;
import com.mockio.core_service.ai.ollama.generator.OllamaFollowUpQuestionValid;
import com.mockio.core_service.ai.openAi.generator.OpenAIFollowUpQuestionValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompositeFollowUpQuestionValid  {

    private final OpenAIFollowUpQuestionValid openAIFollowUpQuestionValid;
    private final OllamaFollowUpQuestionValid ollamaFollowUpQuestionValid;
    private final FakeFollowUpQuestionValid fakeFollowUpQuestionValid;

    @Value("${ai.generator}")
    private String mode;

    public FollowupValid generateValid(FollowUpQuestionCommand command) {
        List<FollowUpQuestionValid> chain = buildChain(mode);

        RuntimeException last = null;
        for (FollowUpQuestionValid g : chain) {
            try {
                return g.generateValid(command);
            } catch (RuntimeException ex) {
                last = ex;

                log.warn("followup question valid generation failed. engine={}, fallbackable={}, cause={}",
                        g.engine(), isFallbackable(ex), ex.toString());

                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<FollowUpQuestionValid> buildChain(String mode) {
        AiEngine primary = parse(mode);

        return switch (primary) {
            case OLLAMA -> List.of(
                    ollamaFollowUpQuestionValid,
                    openAIFollowUpQuestionValid,
                    fakeFollowUpQuestionValid
            );
            case FAKE -> List.of(fakeFollowUpQuestionValid);
            case OPENAI -> List.of(
                    openAIFollowUpQuestionValid,
                    ollamaFollowUpQuestionValid,
                    fakeFollowUpQuestionValid
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

    private FollowupValid fallbackGenerate(FollowUpQuestionCommand command, Throwable t) {
        log.error("ai deepDive fallback triggered. track={}, difficulty={}, cause={}",
                command.interviewTrack(), command.interviewDifficulty(), t.toString());
        return new FollowupValid(
                false,
                "AI 오류",
                "AI 오류"
        );
    }

}
