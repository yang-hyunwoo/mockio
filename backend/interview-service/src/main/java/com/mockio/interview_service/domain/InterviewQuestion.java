package com.mockio.interview_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.interview_service.constant.QuestionStatus;
import com.mockio.interview_service.constant.QuestionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuestionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuestionType type;

    // --- 꼬리 질문 트리 메타 ---

    @Column(name = "parent_question_id")
    private Long parentQuestionId;

    @Column(name = "depth", nullable = false)
    private Integer depth;

    // --- 생성 트리거/중복 방지 ---

    @Column(name = "trigger_answer_id")
    private Long triggerAnswerId;

    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    // --- 평가/비용 메타 (선택) ---

    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private Integer promptTokens;

    private Integer completionTokens;

    @Column(precision = 10, scale = 6)
    private BigDecimal cost;

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
    private InterviewQuestion(Interview interview,
                              Integer seq,
                              String questionText,
                              QuestionStatus status,
                              QuestionType type,
                              Long parentQuestionId,
                              Integer depth,
                              Long triggerAnswerId,
                              String idempotencyKey,
                              String provider,
                              String model,
                              String promptVersion,
                              Double temperature,
                              OffsetDateTime generatedAt
    ) {
        this.interview = interview;
        this.seq = seq;
        this.questionText = questionText;
        this.status = status;
        this.type = type;
        this.parentQuestionId = parentQuestionId;
        this.depth = depth;
        this.triggerAnswerId = triggerAnswerId;
        this.idempotencyKey = idempotencyKey;
        this.provider = provider;
        this.model = model;
        this.promptVersion = promptVersion;
        this.temperature = temperature;
        this.generatedAt = generatedAt;
    }

    /** 초기(기본) 질문 생성 */
    public static InterviewQuestion createInterviewQuestion(
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
                .status(QuestionStatus.READY)
                .type(QuestionType.BASE)
                .parentQuestionId(null)
                .depth(0)
                .provider(provider)
                .model(model)
                .promptVersion(promptVersion)
                .temperature(temperature)
                .generatedAt(generatedAt)
                .build();
    }

    /** 꼬리(후속) 질문 생성 */
    public static InterviewQuestion createFollowUp(
            Interview interview,
            int seq,
            Long parentQuestionId,
            int parentDepth,
            Long triggerAnswerId,
            String idempotencyKey,
            String questionText,
            String provider,
            String model,
            String promptVersion,
            Double temperature
    ) {
        return InterviewQuestion.builder()
                .interview(interview)
                .seq(seq)
                .parentQuestionId(parentQuestionId)
                .depth(parentDepth + 1)
                .triggerAnswerId(triggerAnswerId)
                .idempotencyKey(idempotencyKey)
                .questionText(questionText)
                .provider(provider)
                .model(model)
                .promptVersion(promptVersion)
                .temperature(temperature)
                .status(QuestionStatus.READY)
                .type(QuestionType.FOLLOW_UP)
                .generatedAt(OffsetDateTime.now())
                .build();
    }

    // ---- 상태 전이(필요 시) ----

    public void markAsked() {
        this.status = QuestionStatus.ASKED;
    }

    public void markAnswered(Integer score, String feedback) {
        this.status = QuestionStatus.ANSWERED;
        this.score = score;
        this.feedback = feedback;
    }

    public void attachCost(Integer promptTokens, Integer completionTokens, BigDecimal cost) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.cost = cost;
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