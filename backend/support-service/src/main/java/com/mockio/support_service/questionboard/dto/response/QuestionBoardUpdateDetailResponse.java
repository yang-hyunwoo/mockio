package com.mockio.support_service.questionboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;


public record QuestionBoardUpdateDetailResponse(

        @Schema(description = "선택한 면접 ID" , example = "10")
        Long selectedInterviewId,

        @Schema(description = "선택한 질문 ID" , example = "10")
        Long selectedQuestionId,

        @Schema(description = "제목" , example = "제목입니다.")
        String title,

        @Schema(description = "내용" , example = "내용입니다.")
        String content,

        @Schema(description = "점수 공개 여부" , example = "true")
        boolean scoreVisible,

        @Schema(description = "피드백 공개 여부" , example = "false")
        boolean aiFeedbackVisible,

        @Schema(description = "태그" , example = "[]")
        Set<String> tags

) { }
