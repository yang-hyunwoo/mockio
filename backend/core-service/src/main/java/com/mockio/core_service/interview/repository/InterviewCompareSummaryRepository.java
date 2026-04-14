package com.mockio.core_service.interview.repository;

import com.mockio.core_service.interview.domain.InterviewCompareSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewCompareSummaryRepository extends JpaRepository<InterviewCompareSummary,Long> {

    Optional<InterviewCompareSummary> findByCurrentInterviewIdAndPrevInterviewId(Long currentInterviewId,
                                                                                 Long prevInterviewId);
}
