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
import com.mockio.ai_service.ollama.generator.OllamaFeedbackGenerator;
import com.mockio.ai_service.openAi.generator.OpenAIFeedbackGenerator;
import com.mockio.common_ai_contractor.generator.feedback.*;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class CompositeFeedbackGenerator implements FeedbackGenerator {

    private final OpenAIFeedbackGenerator openAi;
    private final OllamaFeedbackGenerator ollama;
//    private final FakeInterviewQuestionGenerator fake;

    @Value("${ai.generator}")
    private String mode;

    @Override
    public GeneratedFeedback generate(GenerateFeedbackCommand command) {

        if ("ollama".equalsIgnoreCase(mode)) {
            return ollama.generate(command);
        }
//        if ("fake".equalsIgnoreCase(mode)) {
//            return fake.generate(command);
//        }

         //기본: openai 시도 -> 실패 시 폴백
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
