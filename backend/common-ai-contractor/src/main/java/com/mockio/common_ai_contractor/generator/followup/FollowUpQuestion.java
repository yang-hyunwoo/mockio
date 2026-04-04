package com.mockio.common_ai_contractor.generator.followup;

/**
 * follow up 질문 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record FollowUpQuestion(

        @Schema(description = "답변", example = "ITEM")
        Item questions

) {
    public record Item(

            @Schema(description = "제목", example = "제목")
            String title,

            @Schema(description = "응답값", example = "{a:b,c:d")
            String body,

            @Schema(description = "중요태그", example = "중요")
            String primaryTag,

            @Schema(description = "태그", example = "[]")
            Set<String> tags,

            @Schema(description = "ai", example = "OPENAI")
            String provider,

            @Schema(description = "ai 모델", example ="GPT")
            String model,

            @Schema(description = "ai 버전", example = "1.0")
            String promptVersion,

            @Schema(description = "연관도", example = "0.2")
            Double temperature

    ) {}
}