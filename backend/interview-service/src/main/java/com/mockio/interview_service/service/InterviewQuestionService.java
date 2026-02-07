package com.mockio.interview_service.service;

import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.interview_service.Mapper.InterviewQuestionMapper;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.domain.UserInterviewSetting;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.forward.ai.AIServiceClient;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import com.mockio.interview_service.repository.InterviewRepository;
import com.mockio.interview_service.repository.UserInterviewSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.*;
import static com.mockio.common_core.constant.CommonErrorEnum.ERR_012;
import static java.time.OffsetDateTime.now;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewRepository interviewRepository;
    private final UserInterviewSettingRepository userInterviewSettingRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final AIServiceClient aiServiceClient;

    @Transactional
    public Long generateInterview(String userId) {
        //1.유저 인터뷰 세팅을 조회 한다.
        UserInterviewSetting userInterviewSetting = userInterviewSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomApiException(ERR_012.getHttpStatus(), ERR_012, ERR_012.getMessage()));

        //2.유저 인터뷰 생성 및 조회 한다.
        Interview returnInterview = interviewRepository.findActiveByUserIdAndStatus(userId, ACTIVE)
                .orElseGet(() -> {
                    Interview interview = Interview.create(
                            userId,
                            userInterviewSetting.getTrack(),
                            userInterviewSetting.getDifficulty(),
                            userInterviewSetting.getFeedbackStyle(),
                            userInterviewSetting.getInterviewMode(),
                            userInterviewSetting.getAnswerTimeSeconds(),
                            userInterviewSetting.getInterviewQuestionCount()
                    );
                    return interviewRepository.save(interview);
                });
        return returnInterview.getId();


    }

    @Transactional
    public InterviewQuestionReadResponse generateAndSaveQuestions(Long interviewId, String userId) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(INTERVIEW_NOT_FOUND.getHttpStatus(), INTERVIEW_NOT_FOUND, INTERVIEW_NOT_FOUND.getMessage()));

        if(interview.getStatus() != ACTIVE) {
            throw new CustomApiException(INVALID_INTERVIEW_STATUS.getHttpStatus(), INVALID_INTERVIEW_STATUS, INVALID_INTERVIEW_STATUS.getMessage());
        }

        // 중복 생성 방지: 정책 1) 이미 생성되었으면 기존 결과 반환
        if (interview.isQuestionGenerated()) {
            return getQuestions(interviewId,userId);
        }

        interview.markGenerating();

        try {
            GenerateQuestionCommand cmd = new GenerateQuestionCommand(
                userId,
                interview.getTrack(),
                interview.getDifficulty(),
                interview.getInterviewMode(),
                interview.getAnswerTimeSeconds(),
                interview.getCount()
            );
            GeneratedQuestion generatedQuestion = aiServiceClient.generateQuestions(cmd);
            questionSave(interview, generatedQuestion);
            interview.markGenerated();

            return getQuestions(interviewId,userId);
        } catch (Exception e) {
            interview.markGenerateFailed(e.getMessage());
            throw new CustomApiException(AI_SERVICE_FAILED.getHttpStatus(), AI_SERVICE_FAILED, AI_SERVICE_FAILED.getMessage());
        }
    }

    protected void questionSave(Interview interview, GeneratedQuestion generatedQuestion) {
        List<InterviewQuestion> entites = new ArrayList<>();
        OffsetDateTime now = now();
        List<GeneratedQuestion.Item> questions = generatedQuestion.questions();
        for(GeneratedQuestion.Item q : questions) {
            entites.add(InterviewQuestion.createInterviewQuestion(
                    interview,
                    q.seq(),
                    q.questionText(),
                    q.provider(),
                    q.model(),
                    q.promptVersion(),
                    q.temperature(),
                    now
            ));
        }
        interviewQuestionRepository.saveAll(entites);
    }

    @Transactional(readOnly = true)
    public InterviewQuestionReadResponse getQuestions(Long interviewId,String userId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new CustomApiException(INTERVIEW_NOT_FOUND.getHttpStatus(), INTERVIEW_NOT_FOUND, INTERVIEW_NOT_FOUND.getMessage()));
        if(interview.getUserId().equals(userId)) {
            return InterviewQuestionMapper.fromList(interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interviewId));
        } else {
            throw new CustomApiException(INTERVIEW_FORBIDDEN.getHttpStatus(), INTERVIEW_FORBIDDEN, INTERVIEW_FORBIDDEN.getMessage());
        }
    }
}
