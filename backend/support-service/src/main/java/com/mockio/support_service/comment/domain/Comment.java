package com.mockio.support_service.comment.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import com.mockio.common_jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.*;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_type", nullable = false)
    private String boardType;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "depth", nullable = false)
    private int depth;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "author_nickname", nullable = false ,length = 30)
    private String authorNickname;

    @Column(name = "content", nullable = false , length = 2000)
    private String content;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;


    @Builder
    protected Comment (
            Long id,
            String boardType,
            Long boardId,
            int depth,
            Long userId,
            String authorNickname,
            String content,
            Long parentId,
            boolean deleted,
            OffsetDateTime deletedAt
    ) {
        this.id = id;
        this.boardType = boardType;
        this.boardId = boardId;
        this.depth = depth;
        this.userId = userId;
        this.authorNickname = authorNickname;
        this.content = content;
        this.parentId = parentId;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public static Comment createComment(
            String boardType,
            Long boardId,
            Long userId,
            String authorNickname,
            String content
    ) {
        return Comment.builder()
                .boardType(boardType)
                .boardId(boardId)
                .depth(0)
                .userId(userId)
                .authorNickname(authorNickname)
                .content(content)
                .build();
    }

    public static Comment createReplyComment(
            Comment parent,
            Long userId,
            String authorNickname,
            String content
    ) {
        return Comment.builder()
                .boardType(parent.boardType)
                .boardId(parent.boardId)
                .depth(parent.depth + 1)
                .userId(userId)
                .authorNickname(authorNickname)
                .content(content)
                .parentId(parent.id)
                .build();
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = now();
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
