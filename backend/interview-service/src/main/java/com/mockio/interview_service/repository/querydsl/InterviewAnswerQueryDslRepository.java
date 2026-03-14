package com.mockio.interview_service.repository.querydsl;

import com.mockio.interview_service.dto.response.InterviewQuestionAnswerDetailResponse;

import java.util.Optional;

public interface InterviewAnswerQueryDslRepository {

    Optional<InterviewQuestionAnswerDetailResponse> interviewAnswerDetail(Long userId, Long questionId);
}
