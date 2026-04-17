package com.mockio.support_service.questionboard.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBoardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_board_id", nullable = false)
    private QuestionBoard questionBoard;

    @Column(name = "interview_id", nullable = false)
    private Long interviewId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "answer_text", nullable = false, columnDefinition = "TEXT")
    private String answerText;

    @Column(name = "question_seq", nullable = false)
    private int questionSeq;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "ai_feedback_summary_text", nullable = false, columnDefinition = "TEXT")
    private String aiFeedbackSummaryText;

    @Column(name = "score_visible", nullable = false)
    private boolean scoreVisible;

    @Column(name = "ai_feedback_visible", nullable = false)
    private boolean aiFeedbackVisible;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "question_board_item_tags",
            joinColumns = @JoinColumn(name = "board_item_id")
    )
    @Column(name = "tag", nullable = false, length = 100)
    private final Set<String> tags = new LinkedHashSet<>();

    @Builder
    private QuestionBoardItem(
            Long interviewId,
            Long questionId,
            int questionSeq,
            String questionText,
            Long answerId,
            String answerText,
            Integer score,
            Integer displayOrder,
            String aiFeedbackSummaryText,
            boolean scoreVisible,
            boolean aiFeedbackVisible
    ) {
        this.interviewId = interviewId;
        this.questionId = questionId;
        this.questionSeq = questionSeq;
        this.questionText = questionText;
        this.answerId = answerId;
        this.answerText = answerText;
        this.score = score;
        this.displayOrder = displayOrder;
        this.aiFeedbackSummaryText = aiFeedbackSummaryText;
        this.scoreVisible = scoreVisible;
        this.aiFeedbackVisible = aiFeedbackVisible;
    }

    public static QuestionBoardItem createQuestionBoardItem(
            Long interviewId,
            Long questionId,
            int questionSeq,
            String questionText,
            Long answerId,
            String answerText,
            Integer score,
            String aiFeedbackSummaryText,
            boolean scoreVisible,
            boolean aiFeedbackVisible
    ) {
        return QuestionBoardItem.builder()
                .interviewId(interviewId)
                .questionId(questionId)
                .questionSeq(questionSeq)
                .questionText(questionText)
                .answerId(answerId)
                .answerText(answerText)
                .score(score)
                .displayOrder(1)
                .aiFeedbackSummaryText(aiFeedbackSummaryText)
                .scoreVisible(scoreVisible)
                .aiFeedbackVisible(aiFeedbackVisible)
                .build();
    }



    void assignBoard(QuestionBoard questionBoard) {
        this.questionBoard = questionBoard;
    }

    public void changeVisibility(boolean scoreVisible, boolean aiFeedbackVisible) {
        this.scoreVisible = scoreVisible;
        this.aiFeedbackVisible = aiFeedbackVisible;
    }

    public void replaceTags(Set<String> tags) {
        this.tags.clear();


        if (tags != null) {
            Set<String> tagsReplace = new LinkedHashSet<>();
            for (String tag : tags) {
                String normalized = tag.trim();

                // # 제거
                normalized = normalized.replaceAll("^#+", "");

                // 공백 제거
                normalized = normalized.replaceAll("\\s+", "");

                tagsReplace.add(normalized);
            }
            this.tags.addAll(tagsReplace);
        }
    }

    public void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            this.tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuestionBoardItem that = (QuestionBoardItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
