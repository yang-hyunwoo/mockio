package com.mockio.core_service.ai.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionValid;
import com.mockio.common_ai_contractor.generator.followup.FollowupValid;
import org.springframework.stereotype.Component;

@Component
public class FakeFollowUpQuestionValid implements FollowUpQuestionValid {

    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public FollowupValid generateValid(FollowUpQuestionCommand command) {
        return new FollowupValid(
                true,
                "페이크_생성",
                "페이크 생성"
        );
    }

}
