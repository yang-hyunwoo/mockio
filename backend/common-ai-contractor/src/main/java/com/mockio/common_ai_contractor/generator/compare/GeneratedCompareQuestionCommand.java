package com.mockio.common_ai_contractor.generator.compare;


import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record GeneratedCompareQuestionCommand(

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step1.class)
        @Schema(description = "직무", example = "BACKEND")
        InterviewTrack track,

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step1.class)
        @Schema(description = "피드백스타일", example = "STRICT")
        InterviewFeedbackStyle feedbackStyle,

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step1.class)
        @Schema(description = "적합성", example = "[]")
        List<String> evaluationCriteria,

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step1.class)
        @Schema(description = "질문", example = "질문")
        String questionTitle,

        @Schema(description = "현재답변", example = "현재답변")
        String currentAnswer,

        @Schema(description = "이전답변", example = "이전답변")
        String prevAnswer

) {}
