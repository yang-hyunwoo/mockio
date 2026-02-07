package com.mockio.ai_service.generator;

import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.ai_service.ollama.generator.OllamaInterviewQuestionGenerator;
import com.mockio.ai_service.ollama.generator.OllamaSummaryFeedbackGenerator;
import com.mockio.ai_service.openAi.generator.OpenAIFeedbackGenerator;
import com.mockio.ai_service.openAi.generator.OpenAISummaryFeedbackGenerator;
import com.mockio.common_ai_contractor.generator.feedback.*;
import com.mockio.common_spring.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class CompositeSummaryFeedbackGenerator implements SummaryFeedbackGenerator {

    private final OpenAISummaryFeedbackGenerator openAi;
    private final OllamaSummaryFeedbackGenerator ollama;
//    private final FakeInterviewQuestionGenerator fake;

    @Value("${ai.generator}")
    private String mode;

    @Override
    public GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command) {

        if ("ollama".equalsIgnoreCase(mode)) {
            return ollama.generate(command);
        }
//        if ("fake".equalsIgnoreCase(mode)) {
//            return fake.generate(command);
//        }

        // 기본: openai 시도 -> 실패 시 폴백
        try {
            return openAi.generate(command);
        } catch (CustomApiException e) {
             if (e.getErrorEnum() == AIErrorEnum.RATE_LIMIT) {
                //TODO : 요청 많을 경우는 어떻게 처리 할지?..
             }
            return ollama.generate(command);
        } catch (Exception e) {
            return ollama.generate(command);
        }
    }
}
