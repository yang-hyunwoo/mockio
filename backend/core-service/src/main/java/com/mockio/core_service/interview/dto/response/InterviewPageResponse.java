package com.mockio.core_service.interview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;

import java.time.OffsetDateTime;

public record InterviewPageResponse(
        Long id,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        OffsetDateTime createdAt,
        int progress,
        String idempotencyKey,
        int totalCount,
        EnumResponse status,
        EnumResponse track,
        EnumResponse difficulty,
        EnumResponse feedbackStyle,
        EnumResponse endReason
){}
