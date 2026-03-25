package com.mockio.support_service.file.repository;

import com.mockio.support_service.file.domain.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDetailRepository extends JpaRepository<FileDetail , Long> {

    Optional<FileDetail> findTopByFileGroupIdAndDeletedFalseOrderByIdDesc(Long fileGroupId);

}
