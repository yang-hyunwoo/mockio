package com.mockio.core_service.interview.repository.querydsl;


import com.mockio.core_service.interview.dto.response.InterviewQuestionAnswerDetailResponse;

import java.util.Optional;

public interface InterviewAnswerQueryDslRepository {

    Optional<InterviewQuestionAnswerDetailResponse> interviewAnswerDetail(Long userId, Long questionId);

}
