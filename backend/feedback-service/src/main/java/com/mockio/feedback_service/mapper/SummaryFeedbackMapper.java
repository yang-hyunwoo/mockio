package com.mockio.feedback_service.mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.domain.InterviewSummaryFeedback;
import com.mockio.feedback_service.dto.response.FeedbackDetailResponse;
import com.mockio.feedback_service.dto.response.FeedbackText;
import com.mockio.feedback_service.dto.response.FeedbackTotalDetailResponse;
import com.mockio.feedback_service.dto.response.SummaryFeedbackText;

import static com.mockio.feedback_service.dto.response.FeedbackTotalDetailResponse.*;

public class SummaryFeedbackMapper {

    public static SummaryFeedback from(InterviewSummaryFeedback summaryFeedback, SummaryFeedbackText summaryFeedbackText) {
        return new SummaryFeedback(
                summaryFeedback.getId(),
                summaryFeedback.getTotalScore(),
                summaryFeedbackText.summaryText(),
                summaryFeedbackText.strengths(),
                summaryFeedbackText.improvements(),
                EnumResponse.of(
                        summaryFeedback.getStatus().name(),
                        summaryFeedback.getStatus().getLabel()
                )
        );
    }

}
