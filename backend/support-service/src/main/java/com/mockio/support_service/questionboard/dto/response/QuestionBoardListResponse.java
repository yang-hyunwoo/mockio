package com.mockio.support_service.questionboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record QuestionBoardListResponse(

        @Schema(description = "질문 목록" , example = "[]")
        List<InterviewItem> interviews,

        @Schema(description = "선택한 면접 ID" , example = "10")
        Long selectedInterviewId,

        @Schema(description = "선택한 질문 ID" , example = "10")
        Long selectedQuestionId,

        @Schema(description = "답변 목록" , example = "[]")
        List<QuestionAnswerItem> questionAnswers

) {
    public record InterviewItem(

            @Schema(description = "면접 ID" , example = "1")
            Long interviewId,

            @Schema(description = "면접 제목" , example = "제목 입니다.")
            String interviewTitle,

            @Schema(description = "생성일" , example = "2026-05-01 12423123")
            LocalDate createdAt

    ) {}

    public record QuestionAnswerItem(

            @Schema(description = "면접 난이도" , example = "10")
            Integer seq,

            @Schema(description = "질문ID" , example = "10")
            Long questionId,

            @Schema(description = "질문" , example = "질문입니다.")
            String question,

            @Schema(description = "답변" , example = "답변입니다.")
            String answer,

            @Schema(description = "점수" , example = "10")
            Integer score,

            @Schema(description = "피드백" , example = "피드백 입니다.")
            String aiFeedbackText

    ) {}

}
