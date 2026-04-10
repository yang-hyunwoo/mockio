package com.mockio.core_service.feedback.dto.response;

import java.util.List;

public record InternalFeedbackImprovement(
        String problem,
        String action,
        String example
) {
}
