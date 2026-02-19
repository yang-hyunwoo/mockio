package com.mockio.faq_service.domain;

import com.mockio.faq_service.constant.FaqType;
import com.mockio.common_jpa.domain.BaseEntity;
import com.mockio.common_jpa.domain.vo.BoardTitle;
import com.mockio.common_jpa.domain.vo.Content;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Column(length = 200)
    private BoardTitle question;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(name = "answer", columnDefinition = "TEXT", nullable = false))
    private Content answer;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FaqType faqType;

    @Column(nullable = false)
    private int sort;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private boolean deleted;

    private OffsetDateTime deletedAt;

    @Builder
    protected FaqBoard(String userId,
                       BoardTitle question,
                       Content answer,
                       FaqType faqType,
                       int sort,
                       boolean visible,
                       boolean deleted
    ) {
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.faqType = faqType;
        this.sort = sort;
        this.visible = visible;
        this.deleted = deleted;
    }

    public static FaqBoard createFaqBoard(String userId,
                                          BoardTitle question,
                                          Content answer,
                                          FaqType faqType,
                                          int sort
    ) {
        return FaqBoard.builder()
                .userId(userId)
                .question(question)
                .answer(answer)
                .faqType(faqType)
                .sort(sort)
                .visible(true)
                .deleted(false)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof FaqBoard that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
            return getClass().hashCode();
    }

}
