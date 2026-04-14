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
import com.mockio.common_ai_contractor.generator.question.GenerateBasicQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.question.InterviewBasicQuestionGenerator;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.fake.FakeInterviewBasicQuestionGenerator;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.core_service.ai.fallback.FallbackQuestionRegistry;
import com.mockio.core_service.ai.ollama.generator.OllamaInterviewBasicQuestionGenerator;
import com.mockio.core_service.ai.openAi.generator.OpenAIInterviewBasicQuestionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompositeInterviewBasicQuestionGenerator  {

    private final OpenAIInterviewBasicQuestionGenerator openAIInterviewBasicQuestionGenerator;
    private final OllamaInterviewBasicQuestionGenerator ollamaInterviewBasicQuestionGenerator;
    private final FakeInterviewBasicQuestionGenerator fakeInterviewBasicQuestionGenerator;

    @Value("${ai.generator}")
    private String mode;

    public GeneratedQuestion generate(GenerateBasicQuestionCommand command) {
        List<InterviewBasicQuestionGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (InterviewBasicQuestionGenerator g : chain) {
            try {
                return g.generate(command);
            } catch (RuntimeException ex) {
                last = ex;

                log.warn("basic question generation failed. engine={}, fallbackable={}, cause={}",
                        g.engine(), isFallbackable(ex), ex.toString());

                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<InterviewBasicQuestionGenerator> buildChain(String mode) {
        AiEngine primary = parse(mode);

        return switch (primary) {
            case OLLAMA -> List.of(
                    ollamaInterviewBasicQuestionGenerator,
                    openAIInterviewBasicQuestionGenerator,
                    fakeInterviewBasicQuestionGenerator
            );
            case FAKE -> List.of(fakeInterviewBasicQuestionGenerator);
            case OPENAI -> List.of(
                    openAIInterviewBasicQuestionGenerator,
                    ollamaInterviewBasicQuestionGenerator,
                    fakeInterviewBasicQuestionGenerator
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

    private GeneratedQuestion fallbackGenerate(GenerateBasicQuestionCommand command, Throwable ex) {
        log.warn("ai generate fallback triggered. track={}, count={}, difficulty={}. cause={}",
                command.track(), command.questionCount(), command.difficulty(), ex.toString());
        List<GeneratedQuestion.Item> fallback = new ArrayList<>();
        int n = command.questionCount();
        List<FallbackQuestion> base = FallbackQuestionRegistry.get(command.track(), command.difficulty());

        for (int i = 0; i < Math.min(n, base.size()); i++) {
            fallback.add(new GeneratedQuestion.Item(
                    base.get(i).question(),
                    base.get(i).primaryTag(),
                    base.get(i).tags(),
                    "FALLBACK",
                    "N/A",
                    "v1",
                    0.0));
        }
        return new GeneratedQuestion(fallback);
    }

}
