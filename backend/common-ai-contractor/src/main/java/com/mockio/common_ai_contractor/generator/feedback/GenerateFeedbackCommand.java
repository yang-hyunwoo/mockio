package com.mockio.common_ai_contractor.generator.feedback;

/**
 * 면접 피드백 DTO
 */

public record GenerateFeedbackCommand(

        //질문
        String questionText,

        //답변
        String answerText,

        //면접 트랙
        String track,

        //면접 난이도
        String difficulty,

        //면접 피드백 스타일
        String feedbackStyle
) {}

