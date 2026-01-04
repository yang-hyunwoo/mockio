package com.mockio.interview_service.repository;

import com.mockio.interview_service.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

}
