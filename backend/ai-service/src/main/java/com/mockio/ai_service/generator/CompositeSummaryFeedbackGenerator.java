package com.mockio.ai_service.generator;

import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.*;
import com.mockio.common_core.exception.CustomApiException;
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
public class CompositeSummaryFeedbackGenerator implements SummaryFeedbackGenerator {

    private final List<SummaryFeedbackGenerator> generators;
    private static final String PROMPT_VERSION = "v1";

    @Value("${ai.generator}")
    private String mode;

    @Override
    public AiEngine engine() {
        return null;
    }

    @Override
    public GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command) {
        List<SummaryFeedbackGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (SummaryFeedbackGenerator g : chain) {
            try {
                return g.generate(command);
            } catch (RuntimeException ex) {
                last = ex;
                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<SummaryFeedbackGenerator> buildChain(String mode) {
        //  openai -> ollama -> fake
        //     ollama -> openai -> fake
        AiEngine primary = parse(mode);

        SummaryFeedbackGenerator openai = find(AiEngine.OPENAI);
        SummaryFeedbackGenerator ollama = find(AiEngine.OLLAMA);
        SummaryFeedbackGenerator fake = find(AiEngine.FAKE);

        return switch (primary) {
            case OLLAMA -> List.of(ollama, openai, fake);
            case FAKE -> List.of(fake);
            default -> List.of(openai, ollama, fake);
        };
    }

    private SummaryFeedbackGenerator find(AiEngine engine) {
        return generators.stream()
                .filter(g -> g.engine() == engine)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing generator: " + engine));
    }

    private AiEngine parse(String mode) {
        if ("ollama".equalsIgnoreCase(mode)) return AiEngine.OLLAMA;
        if ("fake".equalsIgnoreCase(mode)) return AiEngine.FAKE;
        return AiEngine.OPENAI;
    }

    private boolean isFallbackable(Throwable ex) {
        if (ex instanceof CustomApiException cae) {
            return cae.getErrorEnum() == AIErrorEnum.RATE_LIMIT
                    || cae.getErrorEnum() == AIErrorEnum.TEMPORARY_ERROR;
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
                null,
                null,
                "FALLBACK",
                "N/A",
                PROMPT_VERSION,
                0.0
        );
    }

}
