package com.mockio.core_service.interview.dto.response;

import com.mockio.common_ai_contractor.generator.question.Question;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record SelectedQuestion(

        @Schema(description = "질문", example = "제목,내용,질문타입")
        Question question,

        @Schema(description = "주요_태그", example = "태그")
        String primaryTag,

        @Schema(description = "보조_태그", example = "[]")
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
