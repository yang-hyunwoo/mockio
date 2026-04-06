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
        Integer count,
        List<String> primaryTags
) {
    public static InterviewGenerateContext generated() {
        return new InterviewGenerateContext(
                true,
                null,
                null,
                null,
                null,
                null,
                List.of()
        );
    }

    public static InterviewGenerateContext of(
            InterviewTrack track,
            InterviewDifficulty difficulty,
            InterviewMode interviewMode,
            Integer answerTimeSeconds,
            Integer count,
            List<String> primaryTags
    ) {
        return new InterviewGenerateContext(
                false,
                track,
                difficulty,
                interviewMode,
                answerTimeSeconds,
                count,
                primaryTags
        );
    }
}