package com.mockio.ai_service.generator;



import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class CompositeFollowUpQuestionGenerator implements FollowUpQuestionGenerator {

    private final List<FollowUpQuestionGenerator> generators;

    @Value("${ai.generator}")
    private String mode;


    @Override
    public AiEngine engine() {
        return null;
    }

    @Override
    public FollowUpQuestion generate(FollowUpQuestionCommand command) {
        List<FollowUpQuestionGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (FollowUpQuestionGenerator g : chain) {
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

    private List<FollowUpQuestionGenerator> buildChain(String mode) {
        //  openai -> ollama -> fake
        //     ollama -> openai -> fake
        AiEngine primary = parse(mode);

        FollowUpQuestionGenerator openai = find(AiEngine.OPENAI);
        FollowUpQuestionGenerator ollama = find(AiEngine.OLLAMA);
        FollowUpQuestionGenerator fake = find(AiEngine.FAKE);

        return switch (primary) {
            case OLLAMA -> List.of(ollama, openai, fake);
            case FAKE -> List.of(fake);
            default -> List.of(openai, ollama, fake);
        };
    }

    private FollowUpQuestionGenerator find(AiEngine engine) {
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

    private FollowUpQuestion fallbackGenerate(FollowUpQuestionCommand command, Throwable t) {
        String q = "방금 답변에서 핵심 근거를 한 문장으로 요약하고, 그 근거가 깨질 수 있는 반례를 하나 들어보세요.";
        return new FollowUpQuestion(new FollowUpQuestion.Item(
                q, "FALLBACK", "N/A", "v1", 0.0
        ));
    }

}




