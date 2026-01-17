package com.mockio.interview_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record InterviewAnswerRequest(
        @Schema(name="인터뷰_ID" , example = "1") Long interviewId,
        @Schema(name="질문_ID" , example = "1") Long questionId,
        @Schema(name = "답변", example = "아아라라") String answerText,
        @Schema(name = "타이핑 시간", example = "30")Integer answerDurationSeconds
) {
}
