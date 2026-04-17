package com.mockio.support_service.questionboard.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "track", nullable = false)
    private String track;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "read_count")
    private int readCount;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @OneToMany(mappedBy = "questionBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private final List<QuestionBoardItem> items = new ArrayList<>();


    @Builder
    private QuestionBoard(
            Long id,
            String track,
            Long userId,
            String nickname,
            String title,
            String content,
            int readCount,
            Boolean deleted,
            OffsetDateTime deletedAt,
            Long deletedBy
    ) {
        this.id = id;
        this.track = track;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.readCount = readCount;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
    }

    public static QuestionBoard createQuestionBoard(
            String track,
            Long userId,
            String nickname,
            String title,
            String content
    ) {
        return QuestionBoard.builder()
                .track(track)
                .userId(userId)
                .nickname(nickname)
                .title(title)
                .content(content)
                .readCount(0)
                .deleted(false)
                .build();
    }

    public void addItem(QuestionBoardItem item) {
        this.items.add(item);
        item.assignBoard(this);
    }

    public void applyPatch(String title,
                           String content,
                           QuestionBoardItem item,
                           Set<String> tags,
                           boolean scoreVisible,
                           boolean aiFeedbackVisible) {
        ofNullable(title).filter(s -> !s.isBlank()).ifPresent(this::changeTitle);
        changeContent(content);
        item.changeVisibility(scoreVisible, aiFeedbackVisible);
        item.replaceTags(tags);
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void softDelete(Long deletedBy) {
        this.deleted = true;
        this.deletedBy = deletedBy;
        this.deletedAt = OffsetDateTime.now();
    }

    public void addReadCount() {
        this.readCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuestionBoard that = (QuestionBoard) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
