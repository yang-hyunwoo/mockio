package com.mockio.core_service.interview.dto.request;

import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record QuestionCompareRequest(

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(name = "인터뷰_ID", example = "1")
        Long interviewId,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Schema(name = "현재_질문_ID", example = "1")
        Long currentQuestionId,

        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        @Schema(name = "이전_질문_ID", example = "1")
        Long prevQuestionId
) {}
