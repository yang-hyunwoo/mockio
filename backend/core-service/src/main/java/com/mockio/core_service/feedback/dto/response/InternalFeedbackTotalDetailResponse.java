package com.mockio.core_service.feedback.dto.response;



import java.util.List;

public record InternalFeedbackTotalDetailResponse(
        InternalSummaryFeedback summaryFeedback,
        List<InternalFeedbackDetailResponse> feedbacks
) {


}
