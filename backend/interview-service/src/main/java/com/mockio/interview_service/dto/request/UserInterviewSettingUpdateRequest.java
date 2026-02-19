package com.mockio.interview_service.dto.request;

import com.mockio.common_ai_contractor.constant.*;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserInterviewSettingUpdateRequest(
        @Schema(description = "면접_질문_분류", example = "SOFTWARE_ENGINEER" )
        InterviewTrack track,
        @Schema(description = "면접_난이도", example = "EASY" )
        InterviewDifficulty difficulty,
        @Schema(description = "면접_피드백_스타일", example = "COACHING" )
        InterviewFeedbackStyle feedbackStyle,
        @Schema(description = "면접_모드", example = "TEXT" )
        InterviewMode interviewMode,
        @Schema(description = "면접_답변_시간", example = "90" )
        Integer answerTimeSeconds,
        @Schema(description="면접_질문_갯수",example = "5")
        int interviewQuestionCount
) {}
