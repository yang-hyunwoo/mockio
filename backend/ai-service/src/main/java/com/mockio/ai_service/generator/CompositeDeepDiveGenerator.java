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
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.*;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
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
public class CompositeDeepDiveGenerator implements DeepDiveGenerator {

    private final List<DeepDiveGenerator> generators;

    @Value("${ai.generator}")
    private String mode;

    @Override
    public AiEngine engine() {
        return null;
    }

    @Override
    public GeneratedDeepDiveBundle generate(GenerateDeepDiveCommand command) {

        List<DeepDiveGenerator> chain = buildChain(mode);

        RuntimeException last = null;
        for (DeepDiveGenerator g : chain) {
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

    private List<DeepDiveGenerator> buildChain(String mode) {
        //  openai -> ollama -> fake
        //     ollama -> openai -> fake
        AiEngine primary = parse(mode);

        DeepDiveGenerator openai = find(AiEngine.OPENAI);
        DeepDiveGenerator ollama = find(AiEngine.OLLAMA);
        DeepDiveGenerator fake = find(AiEngine.FAKE);

        return switch (primary) {
            case OLLAMA -> List.of(ollama, openai, fake);
            case FAKE -> List.of(fake);
            default -> List.of(openai, ollama, fake);
        };
    }

    private DeepDiveGenerator find(AiEngine engine) {
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


    private GeneratedDeepDiveBundle fallbackGenerate(GenerateDeepDiveCommand command, Throwable ex) {
        log.warn("ai feedback fallback triggered. track={}, difficulty={}, cause={}",
                command.interviewTrack(), command.interviewDifficulty(), ex.toString());

        return new GeneratedDeepDiveBundle(
                new DeepDiveDecision(
                        false,
                        0,
                        List.of(),
                        List.of(),
                        "외부 AI 서버 오류로 피드백을 생성하지 못했습니다."
                )
                ,
                new FollowUpQuestion(
                        new FollowUpQuestion.Item(
                                "외부 AI 서버 오류로 피드백을 생성하지 못했습니다.",
                                "FALLBACK",
                                "N/A",
                                "v1",
                                0.0)
                )
        );
    }

}
