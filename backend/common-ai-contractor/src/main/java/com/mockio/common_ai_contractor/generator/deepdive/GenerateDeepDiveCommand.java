package com.mockio.common_ai_contractor.generator.deepdive;

/**
 * 딥 다이브 ai 요청 DTO
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record GenerateDeepDiveCommand(

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(description = "면접 트랙", example = "HR")
        InterviewTrack interviewTrack,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Schema(description = "면접 난이도", example = "EASY")
        InterviewDifficulty interviewDifficulty,

        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        @Schema(description = "메인 질문", example = "질문")
        String basicQuestion,

        @NotBlank(message = "{default.notBlank}", groups = Step4.class)
        @Schema(description = "메인 답변", example = "답변")
        String basicAnswer,

        @NotBlank(message = "{default.notBlank}", groups = Step5.class)
        @Schema(description = "꼬리질문 질문", example = "꼬리질문")
        String question,

        @NotBlank(message = "{default.notBlank}", groups = Step6.class)
        @Schema(description = "꼬리질문 답변", example = "꼬리답변")
        String answer

) {}

