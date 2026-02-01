package com.mockio.ai_service.generator.move;



import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OllamaFollowUpQuestionGenerator implements FollowUpQuestionGenerator {

    @Override
    public FollowUpQuestion generate(FollowUpQuestionCommand command) {
        return null;
    }
}
