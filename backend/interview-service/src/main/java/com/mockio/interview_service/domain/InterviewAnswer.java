package com.mockio.interview_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "interview_answers")
public class InterviewAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 질문 기준으로 여러 답변(재응답)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private InterviewQuestion question;

    // 1,2,3... 답변 회차(버전)
    @Column(name = "attempt", nullable = false)
    private Integer attempt;

    @Column(name = "answer_text", nullable = false, columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "answer_duration_seconds")
    private Integer answerDurationSeconds;

    @Column(name = "answered_at", nullable = false)
    private OffsetDateTime answeredAt;

    // 현재 대표 답변(조회 편의)
    @Column(name = "is_current", nullable = false)
    private boolean current;

    @Column(name="followup_reason" , nullable = false)
    private String followupReason;

    @Column(name = "idempotency_key", length = 64)
    private String idempotencyKey;

    private OffsetDateTime followupAt;

    @Builder
    private InterviewAnswer(
            Long id,
            InterviewQuestion question,
            Integer attempt,
            String answerText,
            Integer answerDurationSeconds,
            OffsetDateTime answeredAt,
            boolean current,
            String followupReason,
            OffsetDateTime followupAt
    ) {
        this.id = id;
        this.question = question;
        this.attempt = attempt;
        this.answerText = answerText;
        this.answerDurationSeconds = answerDurationSeconds;
        this.answeredAt = answeredAt;
        this.current = current;
        this.followupReason = followupReason;
        this.followupAt = followupAt;
    }

    public static InterviewAnswer createAnswer(
            InterviewQuestion question,
            int attempt,
            String answerText,
            Integer answerDurationSeconds
    ) {
        return InterviewAnswer.builder()
                .question(question)
                .attempt(attempt)
                .answerText(answerText)
                .answerDurationSeconds(answerDurationSeconds)
                .answeredAt(OffsetDateTime.now())
                .current(true)
                .build();
    }

    public void followupUpdate(String followupReason) {
        this.followupReason = followupReason;
        this.followupAt = OffsetDateTime.now();

    }

    public void updateAnswer(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
        this.current = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InterviewAnswer that = (InterviewAnswer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
