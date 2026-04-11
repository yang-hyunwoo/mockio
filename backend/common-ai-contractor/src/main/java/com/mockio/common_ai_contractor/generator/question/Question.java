package com.mockio.common_ai_contractor.generator.question;

import io.swagger.v3.oas.annotations.media.Schema;

public record Question(

        @Schema(description = "제목", example = "제목")
        String title,

        @Schema(description = "내용", example = "내용")
        String body,

        @Schema(description = "질문타입", example = "BASIC")
        String questionType,

        @Schema(description = "기본->심화질문타이틀", example = "")
        String baseOnTitle

) {}
