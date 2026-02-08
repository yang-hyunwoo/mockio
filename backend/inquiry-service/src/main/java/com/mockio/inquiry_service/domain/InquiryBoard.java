package com.mockio.inquiry_service.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import com.mockio.common_jpa.domain.vo.Content;
import com.mockio.inquiry_service.constant.InquiryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Objects;


@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20,nullable = false)
    private InquiryType inquiryType;

    @Column(name = "question_user_id", nullable = false)
    private String questionUserId;

    @Column(length = 500,nullable = false)
    private String questionTitle;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(name = "question_content", columnDefinition = "TEXT", nullable = false))
    private Content questionContent;

    @Column(name = "answer_user_id", nullable = false)
    private String answerUserId;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(name = "answer_content", columnDefinition = "TEXT", nullable = false))
    private Content answerContent;

    private OffsetDateTime answerAt;

    @Builder
    protected InquiryBoard(String questionUserId,
                           String questionTitle,
                           Content questionContent,
                           String answerUserId,
                           Content answerContent,
                           InquiryType inquiryType,
                           OffsetDateTime answerAt
    ) {
       this.questionUserId = questionUserId;
        this.questionTitle = questionTitle;
        this.questionContent = questionContent;
        this.answerUserId = answerUserId;
        this.answerContent = answerContent;
        this.inquiryType = inquiryType;
        this.answerAt = answerAt;
    }

    public static InquiryBoard createInquiryBoard(String questionUserId,
                                                  String questionTitle,
                                                  Content questionContent,
                                                  String answerUserId,
                                                  Content answerContent,
                                                  InquiryType inquiryType
    ) {
        return InquiryBoard.builder()
                .questionUserId(questionUserId)
                .questionTitle(questionTitle)
                .questionContent(questionContent)
                .answerUserId(answerUserId)
                .answerContent(answerContent)
                .inquiryType(inquiryType)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InquiryBoard that = (InquiryBoard) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InquiryBoard{" +
                "id=" + id +
                ", questionUserId='" + questionUserId + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionContent=" + questionContent +
                ", answerUserId='" + answerUserId + '\'' +
                ", answerContent=" + answerContent +
                ", inquiryType=" + inquiryType +
                ", answerAt=" + answerAt +
                '}';
    }
}
