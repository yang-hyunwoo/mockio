package com.mockio.user_service.dto.request;

import com.mockio.user_service.constant.*;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserInterviewPreferenceUpdateRequest(
        @Schema(description = "면접_질문_분료", example = "SOFTWARE_ENGINEER" )
        InterviewTrack track,
        @Schema(description = "면접_난이도", example = "EASY" )
        InterviewDifficulty difficulty,
        @Schema(description = "면접_피드백_스타일", example = "COACHING" )
        FeedbackStyle feedbackStyle,
        @Schema(description = "면접_모드", example = "TEXT" )
        InterviewMode interviewMode,
        @Schema(description = "면접_답변_시간", example = "90" )
        int answerTimeSeconds
) {
}
