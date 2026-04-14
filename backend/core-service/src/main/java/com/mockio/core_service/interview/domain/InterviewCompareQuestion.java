package com.mockio.core_service.interview.domain;


import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareQuestion;
import com.mockio.common_jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name="interview_compare_question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewCompareQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long interviewId;

    private Long currentQuestionId;

    private Long prevQuestionId;

    private String status;

    private String headline;

    private String summary;

    private List<String> strengths;

    private List<String> improvements;

    private List<String> improvementTags;

    private String provider;

    private String model;

    private String promptVersion;

    private Double temperature;

    private String errorMessage;

    @Builder
    private InterviewCompareQuestion(
            Long id,
            Long interviewId,
            Long currentQuestionId,
            Long prevQuestionId,
            String status,
            String headline,
            String summary,
            List<String> strengths,
            List<String> improvements,
            List<String> improvementTags,
            String provider,
            String model,
            String promptVersion,
            Double temperature,
            String errorMessage
    ) {
        this.id = id;
        this.interviewId = interviewId;
        this.currentQuestionId = currentQuestionId;
        this.prevQuestionId = prevQuestionId;
        this.status = status;
        this.headline = headline;
        this.summary = summary;
        this.strengths = strengths;
        this.improvements = improvements;
        this.improvementTags = improvementTags;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
        this.errorMessage = errorMessage;
    }

    public static InterviewCompareQuestion createCompareQuestionPending(
            Long interviewId,
            Long currentQuestionId,
            Long prevQuestionId
    ) {
        return InterviewCompareQuestion.builder()
                .interviewId(interviewId)
                .currentQuestionId(currentQuestionId)
                .prevQuestionId(prevQuestionId)
                .status("PENDING")
                .build();
    }

    /**
     * 비교 질문 생성 완료
     * @param response
     */
    public void complete(GeneratedCompareQuestion response) {
        this.headline =response.headline();
        this.summary = response.summary();
        this.strengths = response.strengths();
        this.improvements = response.improvements();
        this.improvementTags = response.improvementTags();
        this.provider = response.provider();
        this.model = response.model();
        this.promptVersion = response.promptVersion();
        this.temperature = response.temperature();
        this.status = "COMPLETED";
    }

    /**
     * 비교 질문 생성 실패
     * @param errorMessage
     */
    public void fail(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 비교 질문 생성 판단
     * @return
     */
    public boolean isProcessing() {
        return this.status.equals("PROCESSING");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InterviewCompareQuestion that = (InterviewCompareQuestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
