package com.mockio.ai_service.generator;

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

import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.ai_service.fallback.FallbackQuestionRegistry;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.question.InterviewQuestionGenerator;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class CompositeInterviewQuestionGenerator implements InterviewQuestionGenerator {

    private final List<InterviewQuestionGenerator> generators;

    @Value("${ai.generator}")
    private String mode;

    @Override
    public AiEngine engine() {
        return null;
    }

    @Override
    public GeneratedQuestion generate(GenerateQuestionCommand command) {
        List<InterviewQuestionGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (InterviewQuestionGenerator g : chain) {
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

    private List<InterviewQuestionGenerator> buildChain(String mode) {
        //  openai -> ollama -> fake
        //     ollama -> openai -> fake
        AiEngine primary = parse(mode);

        InterviewQuestionGenerator openai = find(AiEngine.OPENAI);
        InterviewQuestionGenerator ollama = find(AiEngine.OLLAMA);
        InterviewQuestionGenerator fake = find(AiEngine.FAKE);

        return switch (primary) {
            case OLLAMA -> List.of(ollama, openai, fake);
            case FAKE -> List.of(fake);
            default -> List.of(openai, ollama, fake);
        };
    }

    private InterviewQuestionGenerator find(AiEngine engine) {
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

    private GeneratedQuestion fallbackGenerate(GenerateQuestionCommand command, Throwable ex) {
        log.warn("ai generate fallback triggered. track={}, count={}, difficulty={}. cause={}",
                command.track(), command.questionCount(), command.difficulty(), ex.toString());
        List<GeneratedQuestion.Item> fallback = new ArrayList<>();
        int n = command.questionCount();
        List<String> base = FallbackQuestionRegistry.get(command.track(), command.difficulty());

        for (int i = 0; i < Math.min(n, base.size()); i++) {
            fallback.add(new GeneratedQuestion.Item(((i + 1) * 10),
                    base.get(i),
                    "FALLBACK",
                    "N/A",
                    "v1",
                    0.0));
        }
        return new GeneratedQuestion(fallback);
    }

}
