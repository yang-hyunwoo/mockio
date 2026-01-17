package com.mockio.interview_service.service;

import com.mockio.common_ai_contractor.generator.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.FollowUpQuestionCommand;
import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.interview_service.Mapper.InterviewQuestionMapper;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.request.InterviewAnswerRequest;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.forward.ai.AIServiceClient;
import com.mockio.interview_service.repository.InterviewAnswerRepository;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import com.mockio.interview_service.repository.InterviewRepository;
import com.mockio.interview_service.util.followup.FollowUpDecider;
import com.mockio.interview_service.util.followup.FollowUpDecision;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class InterviewAnswerService {

    private final InterviewAnswerRepository interviewAnswerRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewRepository interviewRepository;
    private final FollowUpDecider followUpDecider;
    private final AIServiceClient aiServiceClient;

    @Transactional
    public InterviewQuestionReadResponse interviewAnswerSave(String userId, InterviewAnswerRequest interviewAnswerRequest) {

        //인터뷰 질문 권한 체크
        Interview interview = findInterview(userId, interviewAnswerRequest.interviewId());

        //인터뷰 질문 존재 여부 확인
        InterviewQuestion interviewQuestion = interviewQuestionRepository.findByIdAndInterviewId(interviewAnswerRequest.questionId(), interviewAnswerRequest.interviewId())
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        // 2) attempt 계산
        int nextAttempt = interviewAnswerRepository.findMaxAttemptByQuestionId(interviewAnswerRequest.questionId()).orElse(0) + 1;

        //질문을 저장한다.
        InterviewAnswer answer = InterviewAnswer.createAnswer(interviewQuestion,
                nextAttempt,
                interviewAnswerRequest.answerText(),
                interviewAnswerRequest.answerDurationSeconds()
        );

        interviewAnswerRepository.save(answer);

        FollowUpDecision decision = followUpDecider.decide(interviewQuestion, interviewAnswerRequest, interview);

        /**
         * 꼬리 질문 유효성 검사에 걸렸다면
         * ai서비스에서 꼬리 질문을 추가 후 꼬리 질문 리터
         * 그렇지 않다면 다음 seq 리턴
         */
        if (decision.askFollowUp()) {
            FollowUpQuestionCommand followUpQuestionCommand = new FollowUpQuestionCommand(
                    interview.getTrack(),
                    interview.getDifficulty(),
                    interview.getFeedbackStyle(),
                    decision.reason(),
                    new FollowUpQuestionCommand.QAPair(
                            interviewQuestion.getQuestionText(),
                            interviewAnswerRequest.answerText()
                    )
            );

            FollowUpQuestion followUpQuestion = aiServiceClient.generateFollowQuestions(followUpQuestionCommand);
            FollowUpQuestion.Item q = followUpQuestion.questions();

            InterviewQuestion saveFollowQuestion = InterviewQuestion.createFollowUp(
                    interview,
                    (interviewQuestion.getSeq() + 1),
                    interviewQuestion.getId(),
                    interviewQuestion.getDepth(),
                    interviewQuestion.getId(),
                    null,
                    q.questionText(),
                    q.provider(),
                    q.model(),
                    q.promptVersion(),
                    q.temperature()
            );

            interviewQuestionRepository.save(saveFollowQuestion);
            answer.followupUpdate(decision.reason());
            return InterviewQuestionMapper.fromList(List.of(saveFollowQuestion));
        } else {
            InterviewQuestion nextQuestion = interviewQuestionRepository.findFirstByInterviewIdAndSeqGreaterThanOrderBySeqAsc(interviewAnswerRequest.interviewId(), interviewQuestion.getSeq()).orElse(null);
            return InterviewQuestionMapper.fromList(List.of(nextQuestion));
        }

    }

    private Interview findInterview(String userId, Long interviewId) {
        return interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_FORBIDDEN.getHttpStatus(),
                        INTERVIEW_FORBIDDEN,
                        INTERVIEW_FORBIDDEN.getMessage()
                ));
    }

}
