package com.mockio.file_service.repository;

import com.mockio.file_service.domain.FileGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileGroupRepository extends JpaRepository<FileGroup , Long> {

    Optional<FileGroup> findByIdAndDomainIdAndDomainType(Long fileId,
                                                         Long domainId,
                                                         String domainType);
}
