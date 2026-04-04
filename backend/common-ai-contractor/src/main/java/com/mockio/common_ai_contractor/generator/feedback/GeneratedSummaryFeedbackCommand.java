package com.mockio.common_ai_contractor.generator.feedback;

/**
 * 면접 총 평가 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record GeneratedSummaryFeedbackCommand(

        @NotBlank(message = "{default.notBlank}", groups = Step6.class)
        @Schema(description = "면접 ID", example = "1")
        Long interviewId,

        @NotBlank(message = "{default.notBlank}", groups = Step7.class)
        @Schema(description = "면접 트랙", example = "HR")
        String track,

        @NotBlank(message = "{default.notBlank}", groups = Step8.class)
        @Schema(description = "면접 난이도", example = "EASY")
        String difficulty,

        @NotBlank(message = "{default.notBlank}", groups = Step9.class)
        @Schema(description = "면접 스타일", example = "STRICT")
        String feedbackStyle,

        @NotBlank(message = "{default.notBlank}", groups = Step10.class)
        @Schema(description = "면접 답변 리스트", example = "[]")
        List<Item> items

) {
    public record Item(

            @NotBlank(message = "{default.notBlank}", groups = Step1.class)
            @Schema(description = "답변 ID", example = "1")
            Long answerId,

            @NotBlank(message = "{default.notBlank}", groups = Step2.class)
            @Schema(description = "attempt", example = "1")
            Integer attempt,

            @NotBlank(message = "{default.notBlank}", groups = Step3.class)
            @Schema(description = "면접 질문", example = "질문")
            String questionText,

            @Schema(description = "면접 답변", example = "답변")
            String answerText,

            @NotBlank(message = "{default.notBlank}", groups = Step5.class)
            @Schema(description = "답변 등록 시간", example = "10")
            Integer answerDurationSeconds

    ) {}
}
