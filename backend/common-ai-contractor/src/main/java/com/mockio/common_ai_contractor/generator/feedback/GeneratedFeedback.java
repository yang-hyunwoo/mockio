package com.mockio.common_ai_contractor.generator.feedback;

/**
 * follow up ai DTO
 */

public record GeneratedFeedback(

        //피드백 답변
        String feedbackText,

        //점수
        Integer score,

        //ai
        String provider,

        //ai 모델
        String model,

        //ai 버전
        String promptVersion,

        //연관도
        Double temperature
) {}
