package com.mockio.core_service.interview.dto.request;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

import java.util.List;

public record InterviewGenerateContext(
        boolean alreadyGenerated,
        InterviewTrack track,
        InterviewDifficulty difficulty,
        InterviewMode interviewMode,
        Integer answerTimeSeconds,
        Integer count
) {
    public static InterviewGenerateContext generated() {
        return new InterviewGenerateContext(
                true,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static InterviewGenerateContext of(
            InterviewTrack track,
            InterviewDifficulty difficulty,
            InterviewMode interviewMode,
            Integer answerTimeSeconds,
            Integer count
    ) {
        return new InterviewGenerateContext(
                false,
                track,
                difficulty,
                interviewMode,
                answerTimeSeconds,
                count
        );
    }
}