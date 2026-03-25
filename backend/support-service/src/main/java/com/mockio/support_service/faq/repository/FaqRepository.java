package com.mockio.support_service.faq.repository;

import com.mockio.support_service.faq.domain.FaqBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<FaqBoard,Long> {
}
