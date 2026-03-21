package com.mockio.feedback_service.mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.domain.InterviewSummaryFeedback;
import com.mockio.feedback_service.dto.response.*;

import java.util.List;

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
                ),
                summaryFeedbackText.dimensions()
        );
    }

    public static InterviewScoreListResponse fromScoreList(List<InterviewSummaryFeedback> summaryFeedbacks) {

        List<InterviewScoreListResponse.Item> items = summaryFeedbacks.stream()
                .map(f -> new InterviewScoreListResponse.Item(
                        f.getId(),  // 또는 f.getInterviewId()
                        f.getTotalScore()
                ))
                .toList();
        return new InterviewScoreListResponse(items);
    }

}
