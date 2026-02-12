package com.mockio.common_ai_contractor.generator.followup;

import com.mockio.common_ai_contractor.constant.AiEngine;

public interface FollowUpQuestionGenerator {
    AiEngine engine();
    FollowUpQuestion generate(FollowUpQuestionCommand command);
}