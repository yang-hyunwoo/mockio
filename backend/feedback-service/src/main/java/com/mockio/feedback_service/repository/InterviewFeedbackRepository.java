package com.mockio.feedback_service.repository;

import com.mockio.feedback_service.domain.InterviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewFeedbackRepository extends JpaRepository<InterviewFeedback , Long> {

    Optional<InterviewFeedback> findByAnswerId(Long answerId);
}
