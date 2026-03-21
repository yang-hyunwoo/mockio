package com.mockio.feedback_service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.domain.InterviewSummaryFeedback;
import com.mockio.feedback_service.dto.response.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mockio.feedback_service.dto.response.FeedbackTotalDetailResponse.*;

public class SummaryFeedbackMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static SummaryFeedback from(InterviewSummaryFeedback summaryFeedback, SummaryFeedbackText summaryFeedbackText) {
        return new SummaryFeedback(
                summaryFeedback.getId(),
                summaryFeedback.getTotalScore(),
                summaryFeedbackText.summaryText(),
                summaryFeedbackText.strengths(),
                summaryFeedbackText.improvements(),
                EnumResponse.of(
                        summaryFeedback.getStatus().name(),
                        summaryFeedback.getStatus().getLabel()
                ),
                summaryFeedbackText.dimensions()
        );
    }

    public static InterviewScoreListResponse fromScoreList(List<InterviewSummaryFeedback> summaryFeedbacks) {
        List<InterviewScoreListResponse.Item> items = summaryFeedbacks.stream()
                .map(SummaryFeedbackMapper::toItem)
                .toList();

        return new InterviewScoreListResponse(items);
    }

    private static InterviewScoreListResponse.Item toItem(InterviewSummaryFeedback f) {
        try {
            SummaryFeedbackText feedbackText =
                    objectMapper.readValue(f.getSummaryFeedbackText(), SummaryFeedbackText.class);
            var d = feedbackText.dimensions();
            return new InterviewScoreListResponse.Item(
                    f.getId(),
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
