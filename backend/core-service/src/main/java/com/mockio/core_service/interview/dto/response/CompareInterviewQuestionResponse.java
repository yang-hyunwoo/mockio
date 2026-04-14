package com.mockio.core_service.interview.dto.response;

import java.util.List;

public record CompareInterviewQuestionResponse(
    Long id,
    Long currentQuestionId,
    String headline,
    String summary,
    List<String> strengths,
    List<String> improvements,
    String status,
    String errorMessage
) {
}
