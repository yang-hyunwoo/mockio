package com.mockio.interview_service.dto.response;

import com.mockio.common_spring.constant.Status;
import com.mockio.common_spring.util.response.EnumResponse;

import java.util.List;

public record FeedbackDetailResponse(
        Long id,
        Integer score,
        String summary,
        List<String> strengths,
        List<String> improvements,
        String modelAnswer,
        EnumResponse status
) {}
