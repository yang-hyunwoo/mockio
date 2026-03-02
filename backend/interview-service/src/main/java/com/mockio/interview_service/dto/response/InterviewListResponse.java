package com.mockio.interview_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewListResponse (
        List<Item> interviews
){
    public record Item(
            Long id,
            String title,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            OffsetDateTime createdAt,
            int progress
    ) {

    }
}
