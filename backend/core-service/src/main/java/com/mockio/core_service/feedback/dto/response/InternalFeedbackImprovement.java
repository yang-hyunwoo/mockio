package com.mockio.core_service.feedback.dto.response;


public record InternalFeedbackImprovement(
        String problem,
        String action,
        String example
) {
}
