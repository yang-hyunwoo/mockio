package com.mockio.core_service.interview.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewAnswer;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.response.*;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                            feedback != null ? feedback.improvementTags() : null,
                            feedback != null ? feedback.jobMetrics() : null
                    );
                })
                .toList();

        SummaryFeedback summary =
                feedbackTotalDetailResponse == null ? null : feedbackTotalDetailResponse.summaryFeedback();
        Integer totalScore = summary != null ? summary.totalScore() : null;
        String summaryText = summary != null ? summary.summaryText() : null;
        FeedbackDimensions feedbackDimensions = summary != null ? summary.feedbackDimensions() : null;
        FeedbackJobMetric feedbackJobMetric = summary != null ? summary.feedbackJobMetrics() : null;

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
                feedbackJobMetric,
                EnumResponse.of(
                        interview.getEndReason().name(),
                        interview.getEndReason().getLabel()
                )
        );
    }


    public static InterviewScoreHistoryResponse.Item fromScoreHistory(
            Interview interview,
            Map<Long, InterviewScoreListItem> scoreMap
    ) {
        System.out.println("interviewId = " + interview.getId());
        System.out.println("scoreMap keys = " + scoreMap.keySet());

        InterviewScoreListItem item = scoreMap.get(interview.getId());
        System.out.println("found item = " + item);

        int score = (item != null) ? item.score() : 0;

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
            Map<Long, InterviewScoreListItem> scoreMap
    ) {
        return new InterviewScoreHistoryResponse(
                interviews.stream()
                        .map(interview -> fromScoreHistory(interview, scoreMap))
                        .toList()
        );
    }

    public static InterviewHistoryResponse fromHistoryList(
            List<Interview> interviews,
            Map<Long , InterviewScoreListItem> scoreMap
    ) {
        return new InterviewHistoryResponse(
                interviews.stream()
                        .map(interview -> fromHistory(interview, scoreMap))
                        .toList()
        );
    }

    public static InterviewHistoryResponse.Item fromHistory(
            Interview interview,
            Map<Long, InterviewScoreListItem> scoreMap
    ) {
        InterviewScoreListItem item = scoreMap.get(interview.getId());
        int score = (item != null) ? item.score() : 0;

        return new InterviewHistoryResponse.Item(
                interview.getId(),
                interview.getTrack().getLabel() + " (" + interview.getDifficulty().getLabel() + ")",
                interview.getTotalCount(),
                EnumResponse.of(
                        interview.getStatus().name(),
                        interview.getStatus().getLabel()
                ),
                EnumResponse.of(
                        interview.getEndReason().name(),
                        interview.getEndReason().getLabel()
                ),
                EnumResponse.of(
                        interview.getTrack().name(),
                        interview.getTrack().getLabel()
                ),
                score,
                interview.getCreatedAt()
        );
    }

    public static WeakPointResponse fromWeakPoint(List<InterviewScoreListItem> items) {
        if (items == null || items.isEmpty()) {
            return new WeakPointResponse(List.of());
        }

        int structureAvg = (int) Math.round(
                items.stream()
                        .mapToInt(InterviewScoreListItem::structure)
                        .average()
                        .orElse(0)
        );

        int clarityAvg = (int) Math.round(
                items.stream()
                        .mapToInt(InterviewScoreListItem::clarity)
                        .average()
                        .orElse(0)
        );

        int specificityAvg = (int) Math.round(
                items.stream()
                        .mapToInt(InterviewScoreListItem::specificity)
                        .average()
                        .orElse(0)
        );

        List<WeakPointResponse.Item> weakPointList = Stream.of(
                        new WeakPointResponse.Item(
                                "STRUCTURE",
                                "구조성",
                                structureAvg,
                                getMessage("STRUCTURE",structureAvg)
                        ),
                        new WeakPointResponse.Item(
                                "CLARITY",
                                "명확성",
                                clarityAvg,
                                getMessage("CLARITY",clarityAvg)
                        ),
                        new WeakPointResponse.Item(
                                "SPECIFICITY",
                                "구체성",
                                specificityAvg,
                                getMessage("SPECIFICITY",specificityAvg)
                        )
                )
                .sorted(Comparator.comparingInt(WeakPointResponse.Item::averageScore))
                .toList();

        return new WeakPointResponse(weakPointList);
    }

    private static String getMessage(String type, int score) {
        return switch (type) {
            case "STRUCTURE" -> getStructureMessage(score);
            case "CLARITY" -> getClarityMessage(score);
            case "SPECIFICITY" -> getSpecificityMessage(score);
            default -> "분석 결과를 확인해보세요.";
        };
    }

    private static String getStructureMessage(int score) {
        if (score >= 80) {
            return "답변 구조가 매우 안정적입니다. 현재 흐름을 유지하세요.";
        }
        if (score >= 65) {
            return "전체 흐름은 괜찮지만, 서론-본론-결론 구성을 더 명확히 해보세요.";
        }
        return "답변 구조가 불명확합니다. 핵심 → 근거 → 결론 순으로 정리해보세요.";
    }

    private static String getClarityMessage(int score) {
        if (score >= 80) {
            return "전달력이 매우 좋습니다. 핵심이 명확하게 드러납니다.";
        }
        if (score >= 65) {
            return "전달은 되지만 다소 장황합니다. 불필요한 표현을 줄여보세요.";
        }
        return "핵심 전달이 부족합니다. 한 문장으로 요약하는 연습이 필요합니다.";
    }

    private static String getSpecificityMessage(int score) {
        if (score >= 80) {
            return "구체적인 사례가 잘 드러납니다. 매우 좋은 답변입니다.";
        }
        if (score >= 65) {
            return "예시는 있으나 디테일이 부족합니다. 수치나 결과를 추가해보세요.";
        }
        return "구체적인 사례가 부족합니다. 실제 경험 기반으로 답변을 보완하세요.";
    }


    public static InterviewHistoryPageResponse fromHistoryResponse(InterviewScoreHistoryResponse scoreResponse,
                                                                   InterviewHistoryResponse historyResponse,
                                                                   Page<Interview> interviewPage,
                                                                   WeakPointResponse weakPointResponse
    ) {
        return new InterviewHistoryPageResponse(
                scoreResponse,
                historyResponse,
                weakPointResponse,
                interviewPage.getNumber(),
                interviewPage.getTotalPages(),
                interviewPage.getTotalElements()
        );
    }

}
