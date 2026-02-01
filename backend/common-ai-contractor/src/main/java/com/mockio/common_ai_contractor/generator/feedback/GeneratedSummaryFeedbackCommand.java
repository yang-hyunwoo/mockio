package com.mockio.common_ai_contractor.generator.feedback;

import java.util.List;

public record GeneratedSummaryFeedbackCommand(
        Long interviewId,
        String track,
        String difficulty,
        String feedbackStyle,
        List<Item> items
) {
    public record Item(
            Long answerId,
            Integer attempt,
            String questionText,
            String answerText,
            Integer answerDurationSeconds
    ) {}
}
