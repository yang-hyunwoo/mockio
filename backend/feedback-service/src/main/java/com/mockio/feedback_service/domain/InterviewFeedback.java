package com.mockio.feedback_service.domain;


import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.common_spring.constant.Status;
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
@Table(name = "interview_feedbacks")
public class InterviewFeedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Interview 서비스의 InterviewAnswer ID
    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    private int failCount;

    private String last_error;

    @Column(name = "feedback_text", nullable = false, columnDefinition = "TEXT")
    private String feedbackText;

    @Column(name = "score")
    private Integer score;

    // AI 메타(피드백 생성 추적용)
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
    private InterviewFeedback(
            Long id,
            Long answerId,
            Status status,
            int failCount,
            String feedbackText,
            Integer score,
            String provider,
            String model,
            String promptVersion,
            Double temperature,
            OffsetDateTime generatedAt
    ) {
        this.id = id;
        this.answerId = answerId;
        this.status = status;
        this.failCount = failCount;
        this.feedbackText = feedbackText;
        this.score = score;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
        this.generatedAt = generatedAt;
    }



    public static InterviewFeedback create(
            Long answerId
    ) {
        return InterviewFeedback.builder()
                .answerId(answerId)
                .status(Status.PENDING)
                .failCount(0)
                .score(0)
                .generatedAt(OffsetDateTime.now())
                .build();
    }

    public boolean successChk() {
        return Status.SUCCEEDED == this.status;
    }

    public void succeed(String feedbackText,
                        Integer score,
                        String provider,
                        String model,
                        String promptVersion,
                        Double temperature
    ) {
        this.feedbackText = feedbackText;
        this.score = score;
        this.status = Status.SUCCEEDED;
        this.provider = provider;
        this.model = model;
        this.failCount = 0;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InterviewFeedback that = (InterviewFeedback) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
