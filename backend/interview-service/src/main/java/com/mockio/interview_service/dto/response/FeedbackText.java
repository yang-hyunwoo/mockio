package com.mockio.interview_service.dto.response;

import java.util.List;

public record FeedbackText(
        Integer score,
        String summary,
        List<String> strengths,
        List<String> improvements,
        String modelAnswer
) {}
