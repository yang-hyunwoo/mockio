package com.mockio.common_ai_contractor.generator.feedback;

/**
 * 면접 총 평가 DTO
 */

import java.util.List;

public record GeneratedSummaryFeedbackCommand(

        //면접 ID
        Long interviewId,

        //면접 트랙
        String track,

        //면접 난이도
        String difficulty,

        //피드백 스타일
        String feedbackStyle,

        List<Item> items
) {
    public record Item(

            //답변 ID
            Long answerId,

            Integer attempt,

            //질문
            String questionText,

            //답변
            String answerText,

            //답변 등록 타임
            Integer answerDurationSeconds
    ) {}
}
