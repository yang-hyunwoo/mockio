package com.mockio.common_ai_contractor.dto;

import java.util.List;

public record FollowUpQuestionResult(
        String question,
        String intent,        // 검증|구체화|반례|트레이드오프|경험
        List<String> tags
) {}
