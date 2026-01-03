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
@Table(name = "interview_questions")
public class InterviewQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @Column(name = "seq", nullable = false)
    private Integer seq;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    // --- AI 생성 메타 (권장) ---
    @Column(name = "provider", length = 30)
    private String provider;     // 예: OPENAI, LOCAL

    @Column(name = "model", length = 100)
    private String model;        // 예: gpt-4.1-mini

    @Column(name = "prompt_version", length = 50)
    private String promptVersion; // 예: v1.0.3

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "generated_at")
    private OffsetDateTime generatedAt;

    @Builder
    private InterviewQuestion(
            Long id,
            Interview interview,
            Integer seq,
            String questionText,
            String provider,
            String model,
            String promptVersion,
            Double temperature,
            OffsetDateTime generatedAt
    ) {
        this.id = id;
        this.interview = interview;
        this.seq = seq;
        this.questionText = questionText;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
        this.generatedAt = generatedAt;
    }

    public static InterviewQuestion create(
            Interview interview,
            int seq,
            String questionText,
            String provider,
            String model,
            String promptVersion,
            Double temperature,
            OffsetDateTime generatedAt
    ) {
        return InterviewQuestion.builder()
                .interview(interview)
                .seq(seq)
                .questionText(questionText)
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
        InterviewQuestion that = (InterviewQuestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
