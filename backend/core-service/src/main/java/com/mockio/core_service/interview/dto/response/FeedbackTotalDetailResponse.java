package com.mockio.core_service.interview.dto.response;



import java.util.List;

public record FeedbackTotalDetailResponse(
        SummaryFeedback summaryFeedback,
        List<FeedbackDetailResponse> feedbacks
) {


}
