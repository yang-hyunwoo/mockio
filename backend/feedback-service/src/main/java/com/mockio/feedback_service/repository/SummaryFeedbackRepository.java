package com.mockio.feedback_service.repository;

import com.mockio.feedback_service.domain.InterviewSummaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SummaryFeedbackRepository extends JpaRepository<InterviewSummaryFeedback , Long> {

    Optional<InterviewSummaryFeedback> findByInterviewId(Long interviewId);
}
