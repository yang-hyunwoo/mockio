package com.mockio.noti_service.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import com.mockio.common_jpa.domain.vo.Content;
import com.mockio.noti_service.constant.NoticePinSort;
import com.mockio.noti_service.constant.NoticeType;
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
public class NoticeBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "thumbnail_file_id")
//    private Files thumbnailFile;

    @Column(nullable = false)
    private String userId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100)
    private String summary;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(name = "content", columnDefinition = "TEXT", nullable = false))
    private Content content;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private NoticeType noticeType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private NoticePinSort noticePinSort;

    @Column(nullable = false)
    private int sort;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Builder
    protected NoticeBoard(String userId,
                          String title,
                          String summary,
                          Content content,
                          NoticeType noticeType,
                          NoticePinSort noticePinSort,
                          int sort,
                          boolean visible,
                          boolean deleted
    ) {
        this.userId = userId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.noticeType = noticeType;
        this.noticePinSort = noticePinSort;
        this.sort = sort;
        this.visible = visible;
        this.deleted = deleted;
    }

    public static NoticeBoard createNoticeBoard(String userId,
                                                String title,
                                                String summary,
                                                Content content,
                                                NoticeType noticeType,
                                                NoticePinSort noticePinSort,
                                                int sort
    ) {
        return NoticeBoard.builder()
                .userId(userId)
                .title(title)
                .summary(summary)
                .content(content)
                .noticeType(noticeType)
                .noticePinSort(noticePinSort)
                .sort(sort)
                .visible(true)
                .deleted(false)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoticeBoard)) return false;
        NoticeBoard that = (NoticeBoard) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "NoticeBoard{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content=" + content +
                ", noticeType=" + noticeType +
                ", noticePinSort=" + noticePinSort +
                ", sort=" + sort +
                ", visible=" + visible +
                ", deleted=" + deleted +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
