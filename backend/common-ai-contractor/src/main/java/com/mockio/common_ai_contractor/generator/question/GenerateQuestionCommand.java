package com.mockio.common_ai_contractor.generator.question;

/**
 * 질문 생성 DTO
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record GenerateQuestionCommand(

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Schema(description = "면접 트랙", example = "HR")
        InterviewTrack track,

        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        @Schema(description = "면접 난이도", example = "EASY")
        InterviewDifficulty difficulty,

        @NotBlank(message = "{default.notBlank}", groups = Step4.class)
        @Schema(description = "면접 답변 모두", example = "VOICE")
        InterviewMode interviewMode,

        @NotBlank(message = "{default.notBlank}", groups = Step5.class)
        @Schema(description = "면접 응답 시간", example = "10")
        Integer answerTimeSeconds,

        @NotBlank(message = "{default.notBlank}", groups = Step6.class)
        @Schema(description = "면접 질문 갯수", example = "1")
        int questionCount

) {}
