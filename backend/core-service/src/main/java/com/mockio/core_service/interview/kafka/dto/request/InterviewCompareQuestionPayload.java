package com.mockio.core_service.interview.kafka.dto.request;

import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

import java.util.List;

public record InterviewCompareQuestionPayload(
        InterviewTrack track,
        InterviewFeedbackStyle feedbackStyle,
        List<String> evaluationCriteria,
        String questionTitle,
        String currentAnswer,
        String prevAnswer
) {
}
