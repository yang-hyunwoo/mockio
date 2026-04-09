package com.mockio.core_service.interview.dto.request;

/**
 * 면접 설정 저장 요청 DTO
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record UserInterviewSettingUpdateRequest(

        @NotBlank(message = "{default.notBlank}",groups = Step1.class)
        @Schema(description = "면접_질문_분류", example = "SOFTWARE_ENGINEER" )
        InterviewTrack track,

        @NotBlank(message = "{default.notBlank}",groups = Step2.class)
        @Schema(description = "면접_난이도", example = "EASY" )
        InterviewDifficulty difficulty,

        @NotBlank(message = "{default.notBlank}",groups = Step3.class)
        @Schema(description = "면접_피드백_스타일", example = "COACHING" )
        InterviewFeedbackStyle feedbackStyle,

        @NotBlank(message = "{default.notBlank}",groups = Step4.class)
        @Schema(description = "면접_모드", example = "TEXT" )
        InterviewMode interviewMode,

        @NotBlank(message = "{default.notBlank}",groups = Step5.class)
        @Schema(description = "면접_답변_시간", example = "90" )
        Integer answerTimeSeconds,

        @NotBlank(message = "{default.notBlank}",groups = Step6.class)
        @Schema(description="면접_질문_갯수",example = "5")
        int questionCount,

        @Schema(description="면접_질문_키워드",example = "5")
        List<String> interviewKeyword

) {}
