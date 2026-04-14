package com.mockio.core_service.ai.generator;


import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.fake.FakeFollowUpQuestionGenerator;
import com.mockio.core_service.ai.ollama.generator.OllamaFollowUpQuestionGenerator;
import com.mockio.core_service.ai.openAi.generator.OpenAIFollowUpQuestionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompositeFollowUpQuestionGenerator {

    private final OpenAIFollowUpQuestionGenerator openAIFollowUpQuestionGenerator;
    private final OllamaFollowUpQuestionGenerator ollamaFollowUpQuestionGenerator;
    private final FakeFollowUpQuestionGenerator fakeFollowUpQuestionGenerator;

    @Value("${ai.generator}")
    private String mode;

    public FollowUpQuestion generate(FollowUpQuestionCommand command) {
        List<FollowUpQuestionGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (FollowUpQuestionGenerator g : chain) {
            try {
                return g.generate(command);
            } catch (RuntimeException ex) {
                last = ex;

                log.warn("followup question generation failed. engine={}, fallbackable={}, cause={}",
                        g.engine(), isFallbackable(ex), ex.toString());

                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<FollowUpQuestionGenerator> buildChain(String mode) {
        AiEngine primary = parse(mode);

        return switch (primary) {
            case OLLAMA -> List.of(
                    ollamaFollowUpQuestionGenerator,
                    openAIFollowUpQuestionGenerator,
                    fakeFollowUpQuestionGenerator
            );
            case FAKE -> List.of(fakeFollowUpQuestionGenerator);
            case OPENAI -> List.of(
                    openAIFollowUpQuestionGenerator,
                    ollamaFollowUpQuestionGenerator,
                    fakeFollowUpQuestionGenerator
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

    private FollowUpQuestion fallbackGenerate(FollowUpQuestionCommand command, Throwable t) {
        String q = "방금 답변에서 핵심 근거를 한 문장으로 요약하고, 그 근거가 깨질 수 있는 반례를 하나 들어보세요.";
        String title = "핵심 근거";
        return new FollowUpQuestion(new FollowUpQuestion.Item(
                title,
                q,
                "반례",
                Set.of("근거", "반례"),
                "FALLBACK",
                "N/A",
                "v1",
                0.0
        ));
    }

}
