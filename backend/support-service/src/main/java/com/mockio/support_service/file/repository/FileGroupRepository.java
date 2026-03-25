package com.mockio.support_service.file.repository;

import com.mockio.support_service.file.domain.FileGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileGroupRepository extends JpaRepository<FileGroup, Long> {

    Optional<FileGroup> findByIdAndDomainIdAndDomainType(Long fileId,
                                                         Long domainId,
                                                         String domainType);

}
