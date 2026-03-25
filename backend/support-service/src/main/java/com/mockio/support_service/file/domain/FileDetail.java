package com.mockio.support_service.file.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_detail")
public class FileDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_group_id", nullable = false)
    private FileGroup fileGroup;

    @Column(nullable = false, length = 255)
    private String originalFileName;

    @Column(nullable = false, length = 255)
    private String storedFileName;

    @Column(nullable = false, length = 1000)
    private String fileUrl;

    private Long fileSize;

    @Column(length = 100)
    private String contentType;

    @Column(length = 50)
    private String provider;

    @Column(length = 255)
    private String publicId;

    @Column(nullable = false)
    private boolean deleted;

    @Builder
    protected FileDetail(String originalFileName,
                         String storedFileName,
                         String fileUrl,
                         Long fileSize,
                         String contentType,
                         String provider,
                         String publicId,
                         boolean deleted
    )  {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.provider = provider;
        this.publicId = publicId;
        this.deleted = deleted;
    }

    public static FileDetail createFileDetail(String originalFileName,
                                              String storedFileName,
                                              String fileUrl,
                                              Long fileSize,
                                              String contentType,
                                              String provider,
                                              String publicId,
                                              boolean deleted
    ) {
        return FileDetail.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .contentType(contentType)
                .provider(provider)
                .publicId(publicId)
                .deleted(deleted)
                .build();
    }

    public void linkToFiles(FileGroup fileGroup) {
        this.fileGroup = fileGroup;

    }

    /**
      * 파일 상세 삭제
     */
    public void updateFileDeleted() {
        this.deleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FileDetail that = (FileDetail) o;
        return Objects.equals(id, that.id) && Objects.equals(fileGroup, that.fileGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileGroup);
    }

    @Override
    public String toString() {
        return "FileDetail{" +
                "id=" + id +
                ", fileGroup=" + fileGroup +
                ", originalFileName='" + originalFileName + '\'' +
                ", storedFileName='" + storedFileName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileSize=" + fileSize +
                ", contentType='" + contentType + '\'' +
                ", provider='" + provider + '\'' +
                ", publicId='" + publicId + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
