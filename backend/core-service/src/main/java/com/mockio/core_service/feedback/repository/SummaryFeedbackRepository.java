package com.mockio.core_service.feedback.repository;

import com.mockio.core_service.feedback.domain.InterviewSummaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SummaryFeedbackRepository extends JpaRepository<InterviewSummaryFeedback , Long> {

    Optional<InterviewSummaryFeedback> findByInterviewId(Long interviewId);

    List<InterviewSummaryFeedback> findByInterviewIdIn(List<Long> interviewId);
}
