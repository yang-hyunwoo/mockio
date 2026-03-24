package com.mockio.core_service.interview.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;

import java.util.List;

public record FeedbackDetailResponse(
        Long id,
        Long answerId,
        Integer score,
        String summary,
        List<String> strengths,
        List<String> improvements,
        String modelAnswer,
        EnumResponse status,
        FeedbackDimensions dimensions,
        String headline,
        List<String> improvementTags
) {}

