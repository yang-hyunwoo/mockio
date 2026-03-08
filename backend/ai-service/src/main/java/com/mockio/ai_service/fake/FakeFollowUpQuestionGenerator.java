package com.mockio.ai_service.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FakeFollowUpQuestionGenerator implements FollowUpQuestionGenerator {


    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public FollowUpQuestion generate(FollowUpQuestionCommand command) {
        return new FollowUpQuestion(
                new FollowUpQuestion.Item(
                        "꼬리물기",
                        "꼬리물기 FAKE",
                        Set.of("꼬리물기"),
                        "FAKE",
                        "FAKE",
                        "0.0",
                        0.0
                )
        );
    }

}
