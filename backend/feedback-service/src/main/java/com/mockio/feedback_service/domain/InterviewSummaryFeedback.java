package com.mockio.feedback_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.common_spring.constant.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "interview_summary_feedbacks")
public class InterviewSummaryFeedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "interview_id", nullable = false)
    private Long interviewId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    private int failCount;

    @Column(name = "summary_feedback_text", nullable = false, columnDefinition = "TEXT")
    private String summaryFeedbackText;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "provider", length = 30)
    private String provider;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "prompt_version", length = 50)
    private String promptVersion;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "generated_at", nullable = false)
    private OffsetDateTime generatedAt;

    @Builder
    private InterviewSummaryFeedback(
            Long id,
            Long interviewId,
            Status status,
            int failCount,
            String summaryFeedbackText,
            Integer totalScore,
            String provider,
            String model,
            String promptVersion,
            Double temperature,
            OffsetDateTime generatedAt
    ) {
        this.id = id;
        this.interviewId = interviewId;
        this.status = status;
        this.failCount = failCount;
        this.summaryFeedbackText = summaryFeedbackText;
        this.totalScore = totalScore;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
        this.generatedAt = generatedAt;
    }

    public static InterviewSummaryFeedback create(
            Long interviewId
    ) {
        return InterviewSummaryFeedback.builder()
                .interviewId(interviewId)
                .status(Status.PENDING)
                .failCount(0)
                .totalScore(0)
                .generatedAt(OffsetDateTime.now())
                .build();
    }

    public boolean successChk() {
        return Status.SUCCEEDED == this.status;
    }

    public void succeed(String summaryFeedbackText,
                        Integer totalScore,
                        String provider,
                        String model,
                        String promptVersion,
                        Double temperature
    ) {
        this.summaryFeedbackText = summaryFeedbackText;
        this.totalScore = totalScore;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
        this.failCount = 0;
        this.status = Status.SUCCEEDED;
    }

    public void skipped() {
        this.summaryFeedbackText = "답변이 없어 종합 피드백을 생성하지 못했습니다.";
        this.totalScore = 0;
        this.status = Status.SKIPPED;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InterviewSummaryFeedback that = (InterviewSummaryFeedback) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}