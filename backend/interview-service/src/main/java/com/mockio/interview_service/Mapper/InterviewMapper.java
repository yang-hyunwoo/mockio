package com.mockio.interview_service.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.response.*;
import org.springframework.data.domain.Page;

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
                ),
                EnumResponse.of(
                        interview.getEndReason() == null ? null : interview.getEndReason().name(),
                        interview.getEndReason() == null ? null : interview.getEndReason().getLabel()
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
                            feedback != null ? feedback.score() : null,
                            EnumResponse.of(
                                    question.getType().name(),
                                    question.getType().getLabel()
                            ),
                            feedback != null ? feedback.strengths() : null,
                            feedback != null ? feedback.improvements() : null,
                            feedback != null ? feedback.modelAnswer() : null,
                            feedback != null ? feedback.dimensions() : null,
                            feedback != null ? feedback.headline() : null,
                            feedback != null ? feedback.improvementTags() : null
                    );
                })
                .toList();
        FeedbackTotalDetailResponse.SummaryFeedback summary =
                feedbackTotalDetailResponse == null ? null : feedbackTotalDetailResponse.summaryFeedback();
        Integer totalScore = summary != null ? summary.totalScore() : null;
        String summaryText = summary != null ? summary.summaryText() : null;
        FeedbackDimensions feedbackDimensions = summary != null ? summary.feedbackDimensions() : null;
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
                questionItems,
                feedbackDimensions,
                EnumResponse.of(
                        interview.getEndReason().name(),
                        interview.getEndReason().getLabel()
                )
        );
    }


    public static InterviewScoreHistoryResponse.Item fromScoreHistory(
            Interview interview,
            Map<Long, Integer> scoreMap
    ) {
        int score = scoreMap.getOrDefault(interview.getId(), 0);

        return new InterviewScoreHistoryResponse.Item(
                interview.getId(),
                interview.getTrack().getLabel() + " (" + interview.getDifficulty().getLabel() + ")",
                EnumResponse.of(
                        interview.getTrack().name(),
                        interview.getTrack().getLabel()
                ),
                score,
                interview.getEndedAt()
        );
    }

    /** 엔티티 리스트 → Response */
    public static InterviewScoreHistoryResponse fromScoreHistoryList(
            List<Interview> interviews,
            Map<Long, Integer> scoreMap
    ) {
        return new InterviewScoreHistoryResponse(
                interviews.stream()
                        .map(interview -> fromScoreHistory(interview, scoreMap))
                        .toList()
        );
    }

    public static InterviewHistoryResponse fromHistoryList(
            List<Interview> interviews,
            Map<Long , Integer> scoreMap
    ) {
        return new InterviewHistoryResponse(
                interviews.stream()
                        .map(interview -> fromHistory(interview, scoreMap))
                        .toList()
        );
    }

    public static InterviewHistoryResponse.Item fromHistory(
            Interview interview,
            Map<Long, Integer> scoreMap
    ) {
        int score = scoreMap.getOrDefault(interview.getId(), 0);

        return new InterviewHistoryResponse.Item(
                interview.getId(),
                interview.getTrack().getLabel() + " (" + interview.getDifficulty().getLabel() + ")",
                EnumResponse.of(
                        interview.getTrack().name(),
                        interview.getTrack().getLabel()
                ),
                score,
                interview.getCreatedAt()
        );
    }

    public static InterviewHistoryPageResponse fromHistoryResponse(InterviewScoreHistoryResponse scoreResponse,
                                                                   InterviewHistoryResponse historyResponse,
                                                                   Page<Interview> interviewPage
    ) {
        return new InterviewHistoryPageResponse(
                scoreResponse,
                historyResponse,
                interviewPage.getNumber(),
                interviewPage.getTotalPages(),
                interviewPage.getTotalElements()
        );
    }
}
