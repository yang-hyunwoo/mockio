package com.mockio.core_service.ai.generator;



import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_500;

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

    private AiEngine parse(String mode) {
        if ("ollama".equalsIgnoreCase(mode)) return AiEngine.OLLAMA;
        if ("fake".equalsIgnoreCase(mode)) return AiEngine.FAKE;
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

    private FollowUpQuestionGenerator find(AiEngine engine) {
        return generators.stream()
                .filter(g -> g != this)
                .filter(g -> g.engine() == engine)
                .findFirst()
                .orElseThrow(
                        () -> new CustomApiException(
                                ERR_500.getHttpStatus(),
                                ERR_500,
                                "AI를 찾을 수 없습니다."));
    }

}




