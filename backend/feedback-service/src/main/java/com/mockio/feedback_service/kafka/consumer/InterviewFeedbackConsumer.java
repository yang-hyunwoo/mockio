package com.mockio.feedback_service.kafka.consumer;

import com.mockio.common_spring.exception.NonRetryableEventException;
import com.mockio.feedback_service.config.AiFeedbackClient;
import com.mockio.feedback_service.config.InterviewServiceClient;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.feedback_service.kafka.domain.ProcessedEvent;
import com.mockio.feedback_service.kafka.dto.InterviewAnswerSubmittedPayload;
import com.mockio.feedback_service.kafka.dto.InterviewCompletedPayload;
import com.mockio.feedback_service.kafka.dto.InterviewLifecycleEvent;
import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import com.mockio.feedback_service.kafka.dto.response.InterviewAnswerDetailResponse;
import com.mockio.feedback_service.kafka.repository.ProcessedEventRepository;
import com.mockio.feedback_service.kafka.support.InterviewEventParser;
import com.mockio.feedback_service.repository.InterviewFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewFeedbackConsumer {

    private final ProcessedEventRepository processedEventRepository;
    private final InterviewEventParser parser;
    private final InterviewServiceClient interviewServiceClient;
    private final AiFeedbackClient aiFeedbackClient;
    private final FeedbackTxService feedbackTxService;
    private static final String CONSUMER_NAME = "feedback-service.interview-lifecycle";

    @KafkaListener(topics = "interview.lifecycle", groupId = "feedback-service")
    public void onMessage(String messageJson) {
        InterviewLifecycleEvent event;

        try {
            event = parser.parse(messageJson);
        } catch (Exception e) {
            //파싱 불가 → 재시도 의미 없음
            throw new NonRetryableEventException("Invalid message", e);
        }

        try {
            processedEventRepository.save(ProcessedEvent.of(event.eventId(), CONSUMER_NAME));

        } catch (DataIntegrityViolationException e) {
            // 이미 처리됨 → 정상 종료(ACK)
            return;
        }

        switch (event.eventType()) {
            case "InterviewAnswerSubmitted" -> handleAnswerSubmitted(event);
            case "InterviewCompleted"       -> handleInterviewCompleted(event);
            default -> throw new NonRetryableEventException("Unknown eventType=" + event.eventType());
        }
    }

    private void handleAnswerSubmitted(InterviewLifecycleEvent event) {
        InterviewAnswerSubmittedPayload payload = parser.payloadAs(event, InterviewAnswerSubmittedPayload.class);

        //  인터뷰 서비스에서 질문/답변 텍스트 조회
        InterviewAnswerDetailResponse answerDetail = interviewServiceClient.getAnswerDetail(payload.answerId());

        //피드백 생성
        InterviewFeedback interviewFeedback = feedbackTxService.ensurePending(payload.answerId());
        if(interviewFeedback.successChk()) {
            return;
        }

        // AI 호출
        GeneratedFeedback result = aiFeedbackClient.generateQuestionFeedback(
                new GenerateFeedbackCommand(
                        answerDetail.questionText(),
                        answerDetail.answerText(),
                        payload.track(),
                        payload.difficulty(),
                        payload.feedbackStyle()
                )
        );
        feedbackTxService.markSucceeded(interviewFeedback.getAnswerId(), result);

    }

    //TODO 진행중
//    private void handleInterviewCompleted(InterviewLifecycleEvent event) {
//        InterviewCompletedPayload payload = parser.payloadAs(event, InterviewCompletedPayload.class);
//
//        // 인터뷰 전체 답변 목록 조회
//        List<InterviewAnswerDetailResponse> answers = interviewServiceClient.getInterviewList(payload.interviewId());
//
//       aiFeedbackClient.generateSummaryFeedback(answers, payload);
//
//        // 저장: interview_id UNIQUE
//        summaryRepository.upsertByInterviewId(
//                payload.interviewId(),
//                result.summaryText(),
//                result.totalScore(),
//                result.strengths(),
//                result.improvements(),
//                result.provider(),
//                result.model(),
//                result.promptVersion(),
//                result.temperature(),
//                result.generatedAt()
//        );
//    }
}
