package com.mockio.core_service.interview.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CompareInterviewSummaryResponse(

        @Schema(description = "ID", example = "1")
        Long id,

        @Schema(description = "헤드라인", example = "헤드라인")
        String headline,

        @Schema(description = "요약", example = "요약")
        String summary,

        @Schema(description = "강점", example = "[]")
        List<String> strengths,

        @Schema(description = "부족한점", example = "[]")
        List<String> improvements,

        @Schema(description = "부족한점태그", example = "[]")
        List<String> improvementTags
) { }
