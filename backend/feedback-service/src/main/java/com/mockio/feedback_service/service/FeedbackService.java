package com.mockio.feedback_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.InterviewErrorCode;
import com.mockio.common_ai_contractor.constant.InterviewFeedbackStatus;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_spring.constant.Status;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.domain.InterviewSummaryFeedback;
import com.mockio.feedback_service.dto.response.*;
import com.mockio.feedback_service.mapper.FeedbackMapper;
import com.mockio.feedback_service.mapper.SummaryFeedbackMapper;
import com.mockio.feedback_service.repository.FeedbackRepository;
import com.mockio.feedback_service.repository.SummaryFeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static com.mockio.feedback_service.dto.response.FeedbackTotalDetailResponse.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository interviewFeedbackRepository;
    private final SummaryFeedbackRepository summaryFeedbackRepository;
    private final ObjectMapper objectMapper;


    @Transactional(readOnly = true)
    public FeedbackDetailResponse interviewFeedbackRead(Long answerId) {
        InterviewFeedback interviewFeedback = interviewFeedbackRepository.findByAnswerId(answerId)
                .orElse(null);

        if (interviewFeedback == null) {
            return null;
        }

        if (interviewFeedback.getStatus() != Status.SUCCEEDED) {
            return new FeedbackDetailResponse(
                    interviewFeedback.getId(),
                    interviewFeedback.getAnswerId(),
                    null,
                    null,
                    List.of(),
                    List.of(),
                    null,
                    EnumResponse.of(
                            interviewFeedback.getStatus().name(),
                            interviewFeedback.getStatus().getLabel()
                    ),
                    null,
                    null,
                    List.of()
            );
        }

        try {
            FeedbackText feedbackText = objectMapper.readValue(
                    interviewFeedback.getFeedbackText(),
                    FeedbackText.class
            );

            return FeedbackMapper.from(interviewFeedback, feedbackText);

        } catch (Exception e) {
            log.error("feedback parse failed. answerId={}, feedbackId={}", answerId, interviewFeedback.getId(), e);
            throw new CustomApiException(FEEDBACK_NOT_CREATED.getHttpStatus(),
                    FEEDBACK_NOT_CREATED,
                    FEEDBACK_NOT_CREATED.getMessage());
        }
    }

    public FeedbackTotalDetailResponse getFeedbackDetail(Long interviewId) {
        List<InterviewFeedback> feedbacks =
                interviewFeedbackRepository.findByInterviewIdOrderByIdAsc(interviewId);

        List<FeedbackDetailResponse> responses = feedbacks.stream()
                .map(interviewFeedback -> {
                    try {
                        if (interviewFeedback.getFeedbackText() != null )
                                 {

                            FeedbackText feedbackText = objectMapper.readValue(
                                    interviewFeedback.getFeedbackText(),
                                    FeedbackText.class
                            );

                            return FeedbackMapper.from(interviewFeedback, feedbackText);
                        } else {
                           return FeedbackMapper.from(interviewFeedback, null);
                        }
                    } catch (Exception e) {
                        log.error("feedback parse failed. feedbackId={}", interviewFeedback.getId(), e);
                        throw new CustomApiException(
                                FEEDBACK_NOT_CREATED.getHttpStatus(),
                                FEEDBACK_NOT_CREATED,
                                FEEDBACK_NOT_CREATED.getMessage()
                        );
                    }
                })
                .toList();

        InterviewSummaryFeedback interviewSummaryFeedback =
                summaryFeedbackRepository.findByInterviewId(interviewId)
                        .orElse(null);

        SummaryFeedback summaryFeedback = null;
        if(interviewSummaryFeedback == null) {
            summaryFeedback = null;
        } else {
            if (interviewSummaryFeedback.getStatus() == Status.SUCCEEDED ||
                    interviewSummaryFeedback.getStatus() == Status.SKIPPED) {
                try {

                    SummaryFeedbackText summaryFeedbackText = objectMapper.readValue(
                            interviewSummaryFeedback.getSummaryFeedbackText(),
                            SummaryFeedbackText.class
                    );

                    summaryFeedback = SummaryFeedbackMapper.from(
                            interviewSummaryFeedback,
                            summaryFeedbackText
                    );

                } catch (Exception e) {
                    log.error("summaryfeedback parse failed. feedbackId={}", interviewSummaryFeedback.getId(), e);
                    throw new CustomApiException(
                            FEEDBACK_NOT_CREATED.getHttpStatus(),
                            FEEDBACK_NOT_CREATED,
                            FEEDBACK_NOT_CREATED.getMessage()
                    );
                }
            }
        }
        return new FeedbackTotalDetailResponse(summaryFeedback,responses);
    }

    public InterviewScoreListResponse getScoreHistory(List<Long> interviewIds) {
        return SummaryFeedbackMapper.fromScoreList(summaryFeedbackRepository.findByInterviewIdIn(interviewIds));
    }

}
