package com.mockio.common_ai_contractor.generator.followup;

/**
 * follow up 질문 DTO
 */

import java.util.Set;

public record FollowUpQuestion(
        Item questions
) {
    public record Item(

            //제목
            String title,

            //응답값
            String body,

            //중요 태그
            String primaryTag,

            //태그
            Set<String> tags,

            //ai
            String provider,

            //ai 모델
            String model,

            //ai 버전
            String promptVersion,

            //연관도
            Double temperature
    ) {}
}