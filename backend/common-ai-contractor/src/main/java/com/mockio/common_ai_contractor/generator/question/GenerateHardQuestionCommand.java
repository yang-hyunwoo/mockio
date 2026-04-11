package com.mockio.common_ai_contractor.generator.question;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record GenerateHardQuestionCommand(

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step1.class)
        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step2.class)
        @Schema(description = "면접 트랙", example = "HR")
        InterviewTrack track,

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step3.class)
        @Schema(description = "면접 난이도", example = "EASY")
        InterviewDifficulty difficulty,

        @Schema(description = "면접_질문_키워드", example = "[]")
        List<String> interviewKeyword,

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step6.class)
        @Schema(description = "기본 질문에 대한 질문 갯수", example = "1")
        int linkHardCount,

        @Schema(description = "심화 질문 갯수", example = "1")
        int questionCount,

        @Schema(description = "기본_질문_리스트", example = "[]")
        List<BaseQuestionContext> baseQuestions

) {}
