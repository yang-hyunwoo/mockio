package com.mockio.core_service.interview.Mapper;

import com.mockio.core_service.interview.domain.InterviewCompareQuestion;
import com.mockio.core_service.interview.domain.InterviewCompareSummary;
import com.mockio.core_service.interview.dto.response.CompareInterviewQuestionResponse;
import com.mockio.core_service.interview.dto.response.CompareInterviewSummaryResponse;

public class InterviewCompareMapper {

    public static CompareInterviewSummaryResponse fromSummary(InterviewCompareSummary req) {
        return new CompareInterviewSummaryResponse(
                req.getId(),
                req.getHeadline(),
                req.getSummary(),
                req.getStrengths(),
                req.getImprovements(),
                req.getImprovementTags()
        );
    }

    public static CompareInterviewQuestionResponse fromQuestion(InterviewCompareQuestion req) {
        return new CompareInterviewQuestionResponse(
                req.getId(),
                req.getCurrentQuestionId(),
                req.getHeadline(),
                req.getSummary(),
                req.getStrengths(),
                req.getImprovements(),
                req.getStatus(),
                req.getErrorMessage()
        );
    }

}
