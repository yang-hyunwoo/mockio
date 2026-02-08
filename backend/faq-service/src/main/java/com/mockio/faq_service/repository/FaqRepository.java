package com.mockio.faq_service.repository;

import com.mockio.faq_service.domain.FaqBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<FaqBoard,Long> {
}
