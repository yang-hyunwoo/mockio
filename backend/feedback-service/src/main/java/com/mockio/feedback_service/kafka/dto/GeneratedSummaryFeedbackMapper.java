package com.mockio.feedback_service.kafka.dto;

import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedbackCommand;
import com.mockio.feedback_service.kafka.dto.response.InterviewAnswerDetailResponse;

import java.util.List;

public class GeneratedSummaryFeedbackMapper {

    public static GeneratedSummaryFeedbackCommand from(
            InterviewCompletedPayload payload,
            List<InterviewAnswerDetailResponse> answers
    ) {
        List<GeneratedSummaryFeedbackCommand.Item> items = answers.stream()
                .map(a -> new GeneratedSummaryFeedbackCommand.Item(
                        a.answerId(),
                        a.attempt(),
                        a.questionText(),
                        a.answerText(),
                        a.answerDurationSeconds()
                ))
                .toList();

        return new GeneratedSummaryFeedbackCommand(
                payload.interviewId(),
                payload.track(),
                payload.difficulty(),
                payload.feedbackStyle(),
                items
        );
    }

}
