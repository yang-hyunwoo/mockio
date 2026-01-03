package com.mockio.interview_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

    // 한 답변에 대해 피드백을 여러 번(모델 변경/재평가)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "answer_id", nullable = false)
    private InterviewAnswer answer;

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
            InterviewAnswer answer,
            String feedbackText,
            Integer score,
            String provider,
            String model,
            String promptVersion,
            Double temperature,
            OffsetDateTime generatedAt
    ) {
        this.id = id;
        this.answer = answer;
        this.feedbackText = feedbackText;
        this.score = score;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
        this.generatedAt = generatedAt;
    }

    public static InterviewFeedback create(
            InterviewAnswer answer,
            String feedbackText,
            Integer score,
            String provider,
            String model,
            String promptVersion,
            Double temperature,
            OffsetDateTime generatedAt
    ) {
        return InterviewFeedback.builder()
                .answer(answer)
                .feedbackText(feedbackText)
                .score(score)
                .provider(provider)
                .model(model)
                .promptVersion(promptVersion)
                .temperature(temperature)
                .generatedAt(generatedAt)
                .build();
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
