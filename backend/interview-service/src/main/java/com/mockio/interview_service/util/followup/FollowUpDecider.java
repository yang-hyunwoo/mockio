package com.mockio.interview_service.util.followup;

import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;

public interface FollowUpDecider {
    FollowUpDecision decide(InterviewQuestion question, InterviewAnswerRequest req, Interview interview);
}
