package com.mockio.core_service.feedback.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.core_service.feedback.domain.InterviewSummaryFeedback;
import com.mockio.core_service.feedback.dto.response.InternalInterviewScoreItem;
import com.mockio.core_service.feedback.dto.response.InternalSummaryFeedback;
import com.mockio.core_service.feedback.dto.response.InternalInterviewScoreListResponse;
import com.mockio.core_service.feedback.dto.response.SummaryFeedbackText;

import java.util.List;

public class SummaryFeedbackMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static InternalSummaryFeedback from(InterviewSummaryFeedback summaryFeedback, SummaryFeedbackText summaryFeedbackText) {
        return new InternalSummaryFeedback(
                summaryFeedback.getInterviewId(),
                summaryFeedback.getTotalScore(),
                summaryFeedbackText.summaryText(),
                summaryFeedbackText.strengths(),
                summaryFeedbackText.improvements(),
                EnumResponse.of(
                        summaryFeedback.getStatus().name(),
                        summaryFeedback.getStatus().getLabel()
                ),
                summaryFeedbackText.dimensions(),
                summaryFeedbackText.jobMetrics()
        );
    }

    public static InternalInterviewScoreListResponse fromScoreList(List<InterviewSummaryFeedback> summaryFeedbacks) {
        List<InternalInterviewScoreItem> items = summaryFeedbacks.stream()
                .map(SummaryFeedbackMapper::toItem)
                .toList();

        return new InternalInterviewScoreListResponse(items);
    }

    private static InternalInterviewScoreItem toItem(InterviewSummaryFeedback f) {
        try {
            SummaryFeedbackText feedbackText =
                    objectMapper.readValue(f.getSummaryFeedbackText(), SummaryFeedbackText.class);
            var d = feedbackText.dimensions();
            return new InternalInterviewScoreItem(
                    f.getInterviewId(),
                    f.getTotalScore(),
                    d != null ? d.structure() : 0,
                    d != null ? d.clarity() : 0,
                    d != null ? d.specificity() : 0
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("파싱 실패 id=" + f.getId(), e);
        }
    }

}
