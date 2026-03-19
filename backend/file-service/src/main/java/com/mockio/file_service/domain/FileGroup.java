package com.mockio.file_service.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import com.mockio.common_security.config.AuditorAwareConfig;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@EntityListeners(AuditorAwareConfig.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_group")
public class FileGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String domainType;

    @Column(nullable = false)
    private Long domainId;

    @Column(nullable = false)
    private boolean deleted;

    @OneToMany(mappedBy = "fileGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileDetail> fileDetails = new ArrayList<>();

    @Builder
    private FileGroup(Long id,
                      String domainType,
                      Long domainId,
                      boolean deleted

    ) {
        this.id = id;
        this.domainType = domainType;
        this.domainId = domainId;
        this.deleted = deleted;
    }

    public static FileGroup createFileGroup(
            String domainType,
            Long domainId,
            boolean deleted
    ) {
        return FileGroup.builder()
                .domainType(domainType)
                .domainId(domainId)
                .deleted(deleted)
                .build();
    }

    public void addFileDetail(FileDetail fileDetail) {
        this.fileDetails.add(fileDetail);
        fileDetail.linkToFiles(this);
    }

    public static FileGroup partialOf(Long id) {
        return FileGroup.builder()
                .id(id)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FileGroup fileGroup = (FileGroup) o;
        return Objects.equals(id, fileGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FileGroup{" +
                "id=" + id +
                ", domainType='" + domainType + '\'' +
                ", domainId=" + domainId +
                ", deleted=" + deleted +
                ", fileDetails=" + fileDetails +
                '}';
    }
}
