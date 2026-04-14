package com.mockio.core_service.ai.generator;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.SummaryFeedbackGenerator;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.fake.FakeSummaryFeedbackGenerator;
import com.mockio.core_service.ai.ollama.generator.OllamaSummaryFeedbackGenerator;
import com.mockio.core_service.ai.openAi.generator.OpenAISummaryFeedbackGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompositeSummaryFeedbackGenerator {

    private final OpenAISummaryFeedbackGenerator openAISummaryFeedbackGenerator;
    private final OllamaSummaryFeedbackGenerator ollamaSummaryFeedbackGenerator;
    private final FakeSummaryFeedbackGenerator fakeSummaryFeedbackGenerator;

    private static final String PROMPT_VERSION = "v1";

    @Value("${ai.generator}")
    private String mode;

    public GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command) {
        List<SummaryFeedbackGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (SummaryFeedbackGenerator g : chain) {
            try {
                return g.generate(command);
            } catch (RuntimeException ex) {
                last = ex;

                log.warn("summary feedback generation failed. engine={}, fallbackable={}, cause={}",
                        g.engine(), isFallbackable(ex), ex.toString());

                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<SummaryFeedbackGenerator> buildChain(String mode) {
        AiEngine primary = parse(mode);

        return switch (primary) {
            case OLLAMA -> List.of(
                    ollamaSummaryFeedbackGenerator,
                    openAISummaryFeedbackGenerator,
                    fakeSummaryFeedbackGenerator
            );
            case FAKE -> List.of(fakeSummaryFeedbackGenerator);
            case OPENAI -> List.of(
                    openAISummaryFeedbackGenerator,
                    ollamaSummaryFeedbackGenerator,
                    fakeSummaryFeedbackGenerator
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

    private GeneratedSummaryFeedback fallbackGenerate(GeneratedSummaryFeedbackCommand command, Throwable ex) {
        log.warn("OpenAI summary fallback triggered. interviewId={}, cause={}",
                command.interviewId(), ex.toString());

        return new GeneratedSummaryFeedback(
                command.interviewId(),
                "외부 AI 서버 오류로 요약 피드백을 생성하지 못했습니다.",
                0,
                "FALLBACK",
                "N/A",
                PROMPT_VERSION,
                0.0
        );
    }

}
