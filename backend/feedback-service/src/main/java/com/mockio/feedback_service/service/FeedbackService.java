package com.mockio.feedback_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.InterviewErrorCode;
import com.mockio.common_ai_contractor.constant.InterviewFeedbackStatus;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_spring.constant.Status;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.dto.response.FeedbackDetailResponse;
import com.mockio.feedback_service.dto.response.FeedbackText;
import com.mockio.feedback_service.mapper.FeedbackMapper;
import com.mockio.feedback_service.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository interviewFeedbackRepository;
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
                    null,
                    null,
                    List.of(),
                    List.of(),
                    null,
                    EnumResponse.of(
                            Status.PENDING.name(),
                            Status.PENDING.getLabel()
                    )
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


}