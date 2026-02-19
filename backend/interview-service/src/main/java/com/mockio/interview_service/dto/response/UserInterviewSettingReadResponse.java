package com.mockio.interview_service.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserInterviewSettingReadResponse(
    @Schema(description = "면접_설정_pk", example = "1")
    Long id,
    @Schema(description = "면접_분야", example = "DATA")
    EnumResponse track,
    @Schema(description = "면접_난이도", example = "EASY")
    EnumResponse difficulty,
    @Schema(description = "면접_피드백_스타일", example = "STRICT")
    EnumResponse feedbackStyle,
    @Schema(description = "면접_모드", example = "TEXT")
    EnumResponse interviewMode,
    @Schema(description = "면접_답변_시간", example = "90")
    Integer answerTimeSeconds
) {}
