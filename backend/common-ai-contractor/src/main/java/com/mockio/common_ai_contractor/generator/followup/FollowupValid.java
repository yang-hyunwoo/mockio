package com.mockio.common_ai_contractor.generator.followup;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * follow up 생성 여부 확인 요청 DTO
 */

public record FollowupValid(

        //follow up 여부
        @Schema(description = "꼬리질문 여부", example = "true")
        boolean shouldFollowUp,

        // 이유
        @Schema(description = "이유", example = "이유")
        String reason,

        @Schema(description = "포커스", example = "포커스")
        String focus

) {}
