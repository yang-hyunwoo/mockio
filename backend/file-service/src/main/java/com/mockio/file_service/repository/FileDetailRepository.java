package com.mockio.file_service.repository;

import com.mockio.file_service.domain.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDetailRepository extends JpaRepository<FileDetail , Long> {

    Optional<FileDetail> findTopByFileGroupIdAndDeletedFalseOrderByIdDesc(Long fileGroupId);
}
