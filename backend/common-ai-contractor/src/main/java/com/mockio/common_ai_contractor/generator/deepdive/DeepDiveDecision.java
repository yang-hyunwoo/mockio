package com.mockio.common_ai_contractor.generator.deepdive;

/**
 * 딥 다이브 ai 응답 DTO
 */

import java.util.List;

public record DeepDiveDecision(

        //딥다이브 여부
        boolean shouldFollowUp,

        //깊이
        int depth,

        //핵심 관점
        List<String> focus,

        //이유
        List<String> gaps,

        //딥다이브 이유
        String reason
) {}
