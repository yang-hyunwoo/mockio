package com.mockio.common_ai_contractor.generator.compare;

/**
 * 이전 면접 비교 질문 생성 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GeneratedCompareQuestion(

        @Schema(description = "헤드라인", example = "헤드라인")
        String headline,

        @Schema(description = "요약", example = "요약")
        String summary,

        @Schema(description = "강점", example = "[]")
        List<String> strengths,

        @Schema(description = "약점", example = "[]")
        List<String> improvements,

        @Schema(description = "약점태그", example = "[]")
        List<String> improvementTags,

        @Schema(description = "판정", example = "BETTER")
        String verdict,

        @Schema(description = "ai", example = "OPENAI")
        String provider,

        @Schema(description = "ai 모델", example = "gpt")
        String model,

        @Schema(description = "ai 버전", example = "1.0")
        String promptVersion,

        @Schema(description = "연관도", example = "0.2")
        Double temperature

) { }
