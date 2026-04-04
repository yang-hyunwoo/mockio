package com.mockio.common_ai_contractor.generator.followup;

/**
 * follow up 질문 생성 DTO
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record FollowUpQuestionCommand(

        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        @Schema(description = "면접 트랙", example = "HR")
        InterviewTrack interviewTrack,

        @NotBlank(message = "{default.notBlank}", groups = Step4.class)
        @Schema(description = "면접 난이도", example = "EASY")
        InterviewDifficulty interviewDifficulty,

        @NotBlank(message = "{default.notBlank}", groups = Step5.class)
        @Schema(description = "면접 피드백", example = "STRICT")
        InterviewFeedbackStyle feedbackStyle,

        @NotBlank(message = "{default.notBlank}", groups = Step6.class)
        @Schema(description = "꼬리질문 이유", example = "이유")
        String followUpReason,

        @Schema(description = "최근 질문", example = "QAPair")
        QAPair recentQa

) {
    public record QAPair(

            @NotBlank(message = "{default.notBlank}", groups = Step1.class)
            @Schema(description = "면접 질문", example = "답변")
            String question,

            @NotBlank(message = "{default.notBlank}", groups = Step2.class)
            @Schema(description = "면접 답변", example = "답변")
            String answer

    ) {}
}
