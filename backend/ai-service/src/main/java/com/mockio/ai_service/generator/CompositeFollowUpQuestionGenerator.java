package com.mockio.ai_service.generator;



import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.ai_service.generator.move.FakeFollowUpQuestionGenerator;
import com.mockio.ai_service.generator.move.OllamaFollowUpQuestionGenerator;
import com.mockio.ai_service.openAi.generator.OpenAIFollowUpQuestionGenerator;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class CompositeFollowUpQuestionGenerator implements FollowUpQuestionGenerator {

    private final OpenAIFollowUpQuestionGenerator openAi;
    private final OllamaFollowUpQuestionGenerator ollama;
    private final FakeFollowUpQuestionGenerator fake;

    @Value("${ai.generator}")
    private String mode;


    @Override
    public FollowUpQuestion generate(FollowUpQuestionCommand command) {
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
            if (e.getErrorEnum() == AIErrorEnum.RATE_LIMIT) {
                //TODO : 요청 많을 경우는 어떻게 처리 할지?..
            }
            return ollama.generate(command);
        } catch (Exception e) {
            return ollama.generate(command);
        }
    }





}
