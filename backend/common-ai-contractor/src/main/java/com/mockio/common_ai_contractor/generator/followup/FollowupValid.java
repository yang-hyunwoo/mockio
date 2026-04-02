package com.mockio.common_ai_contractor.generator.followup;

/**
 * follow up 생성 여부 확인 요청 DTO
 */

public record FollowupValid(

        //follow up 여부
        boolean shouldFollowUp,

        // 이유
        String reason,

        String focus
) {}
