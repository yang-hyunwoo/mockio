package com.mockio.interview_service.domain;

import com.mockio.common_ai_contractor.constant.*;
import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.common_spring.constant.CommonErrorEnum;
import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.interview_service.constant.QuestionGenerationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static java.time.OffsetDateTime.*;
import static org.springframework.http.HttpStatus.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "interviews")
public class Interview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewTrack track;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_style", nullable = false, length = 30)
    private InterviewFeedbackStyle feedbackStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_mode", nullable = false, length = 30)
    private InterviewMode interviewMode;

    @Column(name = "answer_time_seconds", nullable = false)
    private Integer answerTimeSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_gen_status", nullable = false, length = 20)
    private QuestionGenerationStatus questionGenStatus;

    @Column(name = "question_gen_started_at")
    private OffsetDateTime questionGenStartedAt;

    @Column(name = "question_gen_ended_at")
    private OffsetDateTime questionGenEndedAt;

    @Column(name = "question_gen_error", length = 500)
    private String questionGenError;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_status", nullable = false, length = 30)
    private InterviewFeedbackStatus feedbackStatus;


    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private InterviewEndReason endReason;

    private int count;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;


    @Builder
    private Interview(Long id,
                      String userId,
                      InterviewTrack track,
                      InterviewDifficulty difficulty,
                      InterviewFeedbackStyle feedbackStyle,
                      InterviewMode interviewMode,
                      Integer answerTimeSeconds,
                      QuestionGenerationStatus questionGenStatus,
                      OffsetDateTime questionGenStartedAt,
                      OffsetDateTime questionGenEndedAt,
                      String questionGenError,
                      InterviewStatus status,
                      InterviewFeedbackStatus feedbackStatus,
                      int count,
                      OffsetDateTime startedAt,
                      OffsetDateTime endedAt
    ) {

        this.id = id;
        this.userId = userId;
        this.track = track;
        this.difficulty = difficulty;
        this.feedbackStyle = feedbackStyle;
        this.interviewMode = interviewMode;
        this.answerTimeSeconds = answerTimeSeconds;
        this.questionGenStatus = questionGenStatus;
        this.questionGenStartedAt = questionGenStartedAt;
        this.questionGenEndedAt = questionGenEndedAt;
        this.questionGenError = questionGenError;
        this.status = status;
        this.feedbackStatus = feedbackStatus;
        this.count = count;
        this.startedAt = startedAt;
        this.endedAt = endedAt;

    }

    public static Interview create(
            String userId,
            InterviewTrack track,
            InterviewDifficulty difficulty,
            InterviewFeedbackStyle feedbackStyle,
            InterviewMode interviewMode,
            Integer answerTimeSeconds,
            int count
    ) {
        return Interview.builder()
                .userId(userId)
                .track(track)
                .difficulty(difficulty)
                .feedbackStyle(feedbackStyle)
                .interviewMode(interviewMode)
                .answerTimeSeconds(answerTimeSeconds)
                .questionGenStatus(QuestionGenerationStatus.NONE)
                .status(InterviewStatus.ACTIVE)
                .feedbackStatus(InterviewFeedbackStatus.NONE)
                .count(count)
                .startedAt(now())
                .build();
    }

    public boolean isQuestionGenerated() {
        return this.questionGenStatus == QuestionGenerationStatus.DONE;
    }

    public void markGenerating() {
        if (this.questionGenStatus == QuestionGenerationStatus.DONE) {
            throw new CustomApiException(QUESTIONS_ALREADY_DONE.getHttpStatus(), QUESTIONS_ALREADY_DONE, QUESTIONS_ALREADY_DONE.getMessage());
        }
        if (this.questionGenStatus == QuestionGenerationStatus.GENERATING) {
            throw new CustomApiException(QUESTIONS_ALREADY_GENERATED.getHttpStatus(), QUESTIONS_ALREADY_GENERATED, QUESTIONS_ALREADY_GENERATED.getMessage());
        }
        this.questionGenStatus = QuestionGenerationStatus.GENERATING;
        this.questionGenStartedAt = now();
        this.questionGenError = null;
    }

    public void markGenerated() {
        this.questionGenStatus = QuestionGenerationStatus.DONE;
        this.questionGenEndedAt = now();
    }

    public void markGenerateFailed(String error) {
        this.questionGenStatus = QuestionGenerationStatus.FAILED;
        this.questionGenEndedAt = now();
        this.questionGenError = error;
    }



    public void complete() {
        if (this.status != InterviewStatus.ACTIVE) {
            throw new CustomApiException(INTERNAL_SERVER_ERROR.value(), CommonErrorEnum.ILLEGALSTATE, "Interview can be completed only from IN_PROGRESS status.");
        }
        this.status = InterviewStatus.ENDED;
        this.endedAt = now();
    }

    public void fail(OffsetDateTime now) {
        if (this.status == InterviewStatus.ENDED) {
            throw new CustomApiException(INTERNAL_SERVER_ERROR.value(), CommonErrorEnum.ILLEGALSTATE, "Completed interview cannot be failed.");
        }
        this.status = InterviewStatus.FAILED;
        this.endedAt = (this.endedAt == null) ? now : this.endedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Interview interview = (Interview) o;
        return Objects.equals(id, interview.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
