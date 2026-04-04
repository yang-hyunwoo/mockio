package com.mockio.core_service.interview.dto.response;

/**
 * 인터뷰 질문 조회 응답 DTO
 */

import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

public record InterviewQuestionReadResponse(

        @Schema(description = "질문 리스트", example = "[]")
        List<Item> questions,

        @Schema(description = "인터뷰ID", example = "2")
        Long interviewId,

        @Schema(description = "성공여부", example = "true")
        boolean completed,

        @Schema(description = "답변시간", example = "0")
        Integer answerTimeSeconds,

        @Schema(description = "면접 답변 모드", example = "TEXT")
        EnumResponse interviewMode

) {
    public record Item(

            @Schema(description = "ID", example = "1")
            Long id,

            @Schema(description = "면접 ID", example = "1")
            Long interviewId,

            @Schema(description = "순번", example = "10")
            Integer seq,

            @Schema(description = "제목", example = "제목")
            String title,

            @Schema(description = "질문", example = "질문")
            String questionText,

            @Schema(description = "태그", example = "[]")
            Set<String> tags,

            @Schema(description = "타입", example = "TEXT")
            EnumResponse type

    ) {}
}
