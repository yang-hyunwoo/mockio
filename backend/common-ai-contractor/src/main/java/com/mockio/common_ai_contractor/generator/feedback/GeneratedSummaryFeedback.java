package com.mockio.common_ai_contractor.generator.feedback;

/**
 * 면접 총 평가 피드백 DTO
 */

public record GeneratedSummaryFeedback(

        //면접 ID
        Long interviewId,

        //평가 답변
        String summaryFeedbackText,

        //총합
        Integer totalScore,

        //ai
        String provider,

        //ai 모델
        String model,

        //ai 버전
        String promptVersion,

        //연관도
        Double temperature
) {}