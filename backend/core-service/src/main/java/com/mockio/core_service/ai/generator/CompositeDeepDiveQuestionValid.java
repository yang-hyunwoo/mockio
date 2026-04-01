package com.mockio.core_service.ai.generator;


import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveQuestionValid;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveValid;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Primary
@RequiredArgsConstructor
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

        Optional<DeepDiveQuestionValid> openai = findOptional(AiEngine.OPENAI);
        Optional<DeepDiveQuestionValid> ollama = findOptional(AiEngine.OLLAMA);
        Optional<DeepDiveQuestionValid> fake = findOptional(AiEngine.FAKE);

        return switch (primary) {
            case OLLAMA -> Stream.of(ollama, openai, fake)
                    .flatMap(Optional::stream)
                    .toList();
            case FAKE -> Stream.of(fake)
                    .flatMap(Optional::stream)
                    .toList();
            default -> Stream.of(openai, ollama, fake)
                    .flatMap(Optional::stream)
                    .toList();
        };
    }

    private DeepDiveQuestionValid find(AiEngine engine) {
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
            return cae.getErrorEnum() == AIErrorCodeEnum.RATE_LIMIT
                    || cae.getErrorEnum() == AIErrorCodeEnum.TEMPORARY_ERROR;
        }
        return true;
    }

    private DeepDiveValid fallbackGenerate(GenerateDeepDiveCommand command, Throwable t) {
        return new DeepDiveValid(
                false,
                "AI 오류",
                "AI 오류"
        );
    }

    private Optional<DeepDiveQuestionValid> findOptional(AiEngine engine) {
        return generators.stream()
                .filter(g -> g != this)
                .filter(g -> g.engine() == engine)
                .findFirst();
    }

}




