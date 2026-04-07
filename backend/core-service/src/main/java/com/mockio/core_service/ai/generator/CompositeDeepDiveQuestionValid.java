package com.mockio.core_service.ai.generator;


import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveQuestionValid;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveValid;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_500;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class CompositeDeepDiveQuestionValid implements DeepDiveQuestionValid {

    private final List<DeepDiveQuestionValid> generators;

    @Value("${ai.generator}")
    private String mode;


    @Override
    public AiEngine engine() {
        return null;
    }

    @Override
    public DeepDiveValid generateValid(GenerateDeepDiveCommand command) {
        List<DeepDiveQuestionValid> chain = buildChain(mode);

        RuntimeException last = null;
        for (DeepDiveQuestionValid g : chain) {
            try {
                return g.generateValid(command);
            } catch (RuntimeException ex) {
                last = ex;
                if (!isFallbackable(ex)) {
                    throw ex;
                }
            }
        }
        return fallbackGenerate(command, last);
    }

    private List<DeepDiveQuestionValid> buildChain(String mode) {
        AiEngine primary = parse(mode);

        DeepDiveQuestionValid openai = find(AiEngine.OPENAI);
        DeepDiveQuestionValid ollama = find(AiEngine.OLLAMA);
        DeepDiveQuestionValid fake = find(AiEngine.FAKE);

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

    private DeepDiveValid fallbackGenerate(GenerateDeepDiveCommand command, Throwable t) {
        log.error("ai deepDive fallback triggered. track={}, difficulty={}, cause={}",
                command.interviewTrack(), command.interviewDifficulty(), t.toString());
        return new DeepDiveValid(
                false,
                "AI 오류",
                "AI 오류"
        );
    }

    private DeepDiveQuestionValid find(AiEngine engine) {
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




