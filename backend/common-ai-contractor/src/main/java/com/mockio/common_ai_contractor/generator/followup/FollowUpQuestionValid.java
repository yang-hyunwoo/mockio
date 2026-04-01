package com.mockio.common_ai_contractor.generator.followup;

import com.mockio.common_ai_contractor.constant.AiEngine;

public interface FollowUpQuestionValid {
    AiEngine engine();
    FollowupValid generateValid(FollowUpQuestionCommand command);
}