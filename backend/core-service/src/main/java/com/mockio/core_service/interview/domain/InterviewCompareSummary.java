package com.mockio.core_service.interview.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "interview_compare_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewCompareSummary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_interview_id", nullable = false, unique = true)
    private Long currentInterviewId;

    @Column(name = "prev_interview_id", nullable = false, unique = true)
    private Long prevInterviewId;

    @Column(name = "total_count", nullable = false)
    private int totalCount;

    @Column(name = "better_count", nullable = false)
    private int betterCount;

    @Column(name = "not_count", nullable = false)
    private int notCount;

    @Column(name = "headline", nullable = false, length = 255)
    private String headline;

    @Column(name = "summary", nullable = false, columnDefinition = "text")
    private String summary;

    @Type(JsonType.class)
    @Column(name = "strengths", nullable = false, columnDefinition = "jsonb")
    private List<String> strengths;

    @Type(JsonType.class)
    @Column(name = "improvements", nullable = false, columnDefinition = "jsonb")
    private List<String> improvements;

    @Type(JsonType.class)
    @Column(name = "improvement_tags", nullable = false, columnDefinition = "jsonb")
    private List<String> improvementTags;

    @Column(name = "provider", nullable = false, length = 50)
    private String provider;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @Column(name = "prompt_version", nullable = false, length = 50)
    private String promptVersion;

    @Column(name = "temperature")
    private Double temperature;


    @Builder
    private InterviewCompareSummary(
            Long id,
            Long currentInterviewId,
            Long prevInterviewId,
            int totalCount,
            int betterCount,
            int notCount,
            String headline,
            String summary,
            List<String> strengths,
            List<String> improvements,
            List<String> improvementTags,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {
        this.id = id;
        this.currentInterviewId = currentInterviewId;
        this.prevInterviewId = prevInterviewId;
        this.totalCount = totalCount;
        this.betterCount = betterCount;
        this.notCount = notCount;
        this.headline = headline;
        this.summary = summary;
        this.strengths = strengths;
        this.improvements = improvements;
        this.improvementTags = improvementTags;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
    }

    public static InterviewCompareSummary createInterviewCompareSummary(
            Long currentInterviewId,
            Long prevInterviewId,
            int totalCount,
            int betterCount,
            int notCount,
            String headline,
            String summary,
            List<String> strengths,
            List<String> improvements,
            List<String> improvementTags,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {
        return InterviewCompareSummary.builder()
                .currentInterviewId(currentInterviewId)
                .prevInterviewId(prevInterviewId)
                .totalCount(totalCount)
                .betterCount(betterCount)
                .notCount(notCount)
                .headline(headline)
                .summary(summary)
                .strengths(strengths)
                .improvements(improvements)
                .improvementTags(improvementTags)
                .provider(provider)
                .model(model)
                .promptVersion(promptVersion)
                .temperature(temperature)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InterviewCompareSummary that = (InterviewCompareSummary) o;
        return Objects.equals(id, that.id) && Objects.equals(currentInterviewId, that.currentInterviewId) && Objects.equals(prevInterviewId, that.prevInterviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currentInterviewId, prevInterviewId);
    }

}
