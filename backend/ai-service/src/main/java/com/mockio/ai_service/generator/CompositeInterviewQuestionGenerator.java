package com.mockio.ai_service.generator;

import com.mockio.ai_service.openAi.generator.OpenAIInterviewQuestionGenerator;
import com.mockio.common_ai_contractor.generator.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import com.mockio.common_spring.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Primary
@RequiredArgsConstructor
public class CompositeInterviewQuestionGenerator implements InterviewQuestionGenerator {

    private final OpenAIInterviewQuestionGenerator openAi;
    private final OllamaInterviewQuestionGenerator ollama;
    private final FakeInterviewQuestionGenerator fake;

    @Value("${ai.generator}")
    private String mode;

    @Override
    public List<GeneratedQuestion> generate(GenerateQuestionCommand command) {

        if ("ollama".equalsIgnoreCase(mode)) {
            return ollama.generate(command);
        }
        if ("fake".equalsIgnoreCase(mode)) {
            return fake.generate(command);
        }

        // 기본: openai 시도 -> 실패 시 폴백
        try {
            return openAi.generate(command);
        } catch (CustomApiException e) {
            // 429 등 특정 케이스에만 폴백하고 싶다면 여기서 분기
            // if (e.getErrorCode() == AIErrorEnum.RATE_LIMIT) { ... }
            return ollama.generate(command);
        } catch (Exception e) {
            return ollama.generate(command);
        }
    }
}
