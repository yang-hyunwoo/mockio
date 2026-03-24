package com.mockio.core_service.interview.util.followup;


import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.request.InterviewAnswerRequest;

public interface FollowUpDecider {
    FollowUpDecision decide(InterviewQuestion question, InterviewAnswerRequest req, Interview interview);
}
