package com.mockio.common_ai_contractor.generator.question;

/**
 * 질문 생성 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

public record GeneratedQuestion(

        @Schema(description = "답변 리스트", example = "[]")
       List<Item> questions

) {
    public record Item(

            @Schema(description = "seq", example = "1")
            int seq,

            @Schema(description = "제목", example = "제목")
            String title,

            @Schema(description = "응답값", example = "{d:a,b:c}")
            String body,

            @Schema(description = "중요 태그", example = "중요")
            String primaryTag,

            @Schema(description = "태그", example = "[]")
            Set<String> tags,

            @Schema(description = "ai", example = "OPENAI")
            String provider,

            @Schema(description = "ai 모델", example = "gpt")
            String model,

            @Schema(description = "ai 버전", example = "1.0")
            String promptVersion,

            @Schema(description = "연관도", example = "0.2")
            Double temperature

    ) {}
}