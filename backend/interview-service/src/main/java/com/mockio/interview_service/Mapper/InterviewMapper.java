package com.mockio.interview_service.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.response.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InterviewMapper {

    public static InterviewMainListResponse.Item fromMainItem(Interview interview) {
        int total = interview.getTotalCount();
        int answered = interview.getAnsweredQuestions();

        int progress = (total == 0)
                ? 0
                : (int) Math.floor((answered * 100.0) / total);

        return new InterviewMainListResponse.Item(
                interview.getId(),
                interview.getTrack().getLabel() + " (" + interview.getDifficulty().getLabel() + ")",
                interview.getCreatedAt(),
                progress

        );
    }

    /** 엔티티 리스트 → Response */
    public static InterviewMainListResponse fromMainList(List<Interview> interviews) {
        return new InterviewMainListResponse(
                interviews.stream()
                        .map(InterviewMapper::fromMainItem)
                        .toList()
        );
    }

    public static InterviewPageResponse fromItem(Interview interview) {
        int total = interview.getTotalCount();
        int answered = interview.getAnsweredQuestions();

        int progress = (total == 0)
                ? 0
                : (int) Math.floor((answered * 100.0) / total);

        return new InterviewPageResponse(
                interview.getId(),
                interview.getTrack().getLabel() + " (" + interview.getDifficulty().getLabel() + ")",
                interview.getCreatedAt(),
                progress,
                interview.getIdempotencyKey(),
                interview.getTotalCount(),
                EnumResponse.of(
                        interview.getStatus().name(),
                        interview.getStatus().getLabel()
                ),
                EnumResponse.of(
                        interview.getTrack().name(),
                        interview.getTrack().getLabel()
                ),
                EnumResponse.of(
                        interview.getDifficulty().name(),
                        interview.getDifficulty().getLabel()
                ),
                EnumResponse.of(
                        interview.getFeedbackStyle().name(),
                        interview.getFeedbackStyle().getLabel()
                )
        );
    }

    public static InterviewResultResponse fromResult(Interview interview,
                                                     List<InterviewQuestion> questions,
                                                     List<InterviewAnswer> answers,
                                                     FeedbackTotalDetailResponse feedbackTotalDetailResponse){
        String title = interview.getTrack().getLabel() +
                " (" + interview.getDifficulty().getLabel() + ")";
        int durationSeconds = answers.stream()
                .mapToInt(InterviewAnswer::getAnswerDurationSeconds)
                .sum();

        Map<Long, InterviewAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        answer -> answer.getQuestion().getId(),
                        Function.identity(),
                        (a, b) -> a
                ));

        Map<Long, FeedbackDetailResponse> feedbackMap =
                feedbackTotalDetailResponse == null || feedbackTotalDetailResponse.feedbacks() == null
                        ? Collections.emptyMap()
                        : feedbackTotalDetailResponse.feedbacks().stream()
                        .collect(Collectors.toMap(
                                FeedbackDetailResponse::answerId,
                                Function.identity(),
                                (a, b) -> a
                        ));

        List<InterviewResultResponse.QuestionItem> questionItems = questions.stream()
                .map(question -> {
                    InterviewAnswer answer = answerMap.get(question.getId());
                    FeedbackDetailResponse feedback =
                            answer == null ? null : feedbackMap.get(answer.getId());

                    return new InterviewResultResponse.QuestionItem(
                            question.getId(),
                            question.getSeq(),
                            question.getQuestionText(),
                            answer != null ? answer.getAnswerText() : null,
                            feedback != null ? feedback.summary() : null,
                            feedback != null ? feedback.score() : null
                    );
                })
                .toList();
        FeedbackTotalDetailResponse.SummaryFeedback summary =
                feedbackTotalDetailResponse == null ? null : feedbackTotalDetailResponse.summaryFeedback();
        Integer totalScore = summary != null ? summary.totalScore() : null;
        String summaryText = summary != null ? summary.summaryText() : null;
        List<String> strengths = summary != null ? summary.strengths() : List.of();
        List<String> improvements = summary != null ? summary.improvements() : List.of();
        return new InterviewResultResponse(
                interview.getId(),
                title,
                interview.getStartedAt(),
                interview.getEndedAt(),
                durationSeconds,
                interview.getTotalCount(),
                interview.getAnsweredQuestions(),
                totalScore,
                EnumResponse.of(
                        interview.getStatus().name(),
                        interview.getStatus().getLabel()
                ),
                EnumResponse.of(
                        interview.getTrack().name(),
                        interview.getTrack().getLabel()
                ),
                EnumResponse.of(
                        interview.getDifficulty().name(),
                        interview.getDifficulty().getLabel()
                ),
                EnumResponse.of(
                        interview.getFeedbackStyle().name(),
                        interview.getFeedbackStyle().getLabel()
                ),
                summaryText,
                strengths,
                improvements,
                questionItems
        );
    }


}
