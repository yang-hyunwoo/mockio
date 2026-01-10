package com.mockio.common_ai_contractor.generator;

import com.mockio.common_ai_contractor.dto.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.dto.FollowUpQuestionResult;

public interface FollowUpQuestionGenerator {
    FollowUpQuestionResult generate(FollowUpQuestionCommand command);
}