package com.mockio.core_service.feedback.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record InternalQuestionBoardDetailResponse(

        @Schema(description = "면접직무", example = "BACKEND")
        String track,

        @Schema(description = "면접ID", example = "2")
        Long interviewId,

        @Schema(description = "질문ID", example = "2")
        Long questionId,

        @Schema(description = "질문순번", example = "10")
        Integer seq,

        @Schema(description = "질문", example = "질문입니다.")
        String questionText,

        @Schema(description = "답변ID", example = "1")
        Long answerId,

        @Schema(description = "답변", example = "답변입니다")
        String answerText,

        @Schema(description = "점수", example = "0")
        Integer score,

        @Schema(description = "요약", example = "요약입니다.")
        String summary

) {}
