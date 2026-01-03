package com.mockio.interview_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.common_spring.constant.CommonErrorEnum;
import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.interview_service.constant.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

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
    private FeedbackStyle feedbackStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_mode", nullable = false, length = 30)
    private InterviewMode interviewMode;

    @Column(name = "answer_time_seconds", nullable = false)
    private Integer answerTimeSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewStatus status;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;


    @Builder
    private Interview(
            Long id,
            String userId,
            InterviewTrack track,
            InterviewDifficulty difficulty,
            FeedbackStyle feedbackStyle,
            InterviewMode interviewMode,
            Integer answerTimeSeconds,
            InterviewStatus status,
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
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static Interview create(
            String userId,
            InterviewTrack track,
            InterviewDifficulty difficulty,
            FeedbackStyle feedbackStyle,
            InterviewMode interviewMode,
            Integer answerTimeSeconds
    ) {
        return Interview.builder()
                .userId(userId)
                .track(track)
                .difficulty(difficulty)
                .feedbackStyle(feedbackStyle)
                .interviewMode(interviewMode)
                .answerTimeSeconds(answerTimeSeconds)
                .status(InterviewStatus.CREATED)
                .build();
    }

    public void start(OffsetDateTime now) {
        if (this.status != InterviewStatus.CREATED) {
            throw new CustomApiException(INTERNAL_SERVER_ERROR.value(), CommonErrorEnum.ILLEGALSTATE, "Interview can be started only from CREATED status.");
        }
        this.status = InterviewStatus.IN_PROGRESS;
        this.startedAt = (this.startedAt == null) ? now : this.startedAt;
    }

    public void complete(OffsetDateTime now) {
        if (this.status != InterviewStatus.IN_PROGRESS) {
            throw new CustomApiException(INTERNAL_SERVER_ERROR.value(), CommonErrorEnum.ILLEGALSTATE, "Interview can be completed only from IN_PROGRESS status.");
        }
        this.status = InterviewStatus.COMPLETED;
        this.endedAt = now;
    }

    public void fail(OffsetDateTime now) {
        if (this.status == InterviewStatus.COMPLETED) {
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
