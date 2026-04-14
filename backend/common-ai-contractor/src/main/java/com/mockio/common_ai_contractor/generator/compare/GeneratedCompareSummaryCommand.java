package com.mockio.common_ai_contractor.generator.compare;


import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackDimensions;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackJobMetric;

import java.util.List;

public record GeneratedCompareSummaryCommand(
     InterviewTrack track,
     InterviewFeedbackStyle feedbackStyle,
     Interview prevInterview,
     Interview currentInterview,
     Integer totalCount,
     Integer betterCount,
     Integer notCount,
     List<Item> question

) {
    public record Item(
            String type,
            Long currentQuestionId,
            Long prevQuestionId,
            String title,
            String previousAnswer,
            String currentAnswer,
            Integer previousScore,
            Integer currentScore,
            Integer delta
    ) { }

    public record Interview(
            Integer totalScore,
            FeedbackDimensions feedbackDimensions,
            FeedbackJobMetric feedbackJobMetric,
            String summary
    ) { }
}
