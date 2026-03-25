package com.mockio.core_service.interview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewMainListResponse (
        List<Item> interviews
){
    public record Item(
            Long id,
            String title,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            OffsetDateTime createdAt,
            int progress
    ) {}
}
