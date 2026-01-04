package com.mockio.interview_service.service;

import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.interview_service.Mapper.InterviewQuestionMapper;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.generator.GenerateQuestionCommand;
import com.mockio.interview_service.generator.GeneratedQuestion;
import com.mockio.interview_service.generator.InterviewQuestionGenerator;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import com.mockio.interview_service.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static com.mockio.interview_service.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewQuestionGenerator generator;

    @Transactional
    public InterviewQuestionReadResponse generateAndSaveQuestions(Long interviewId, String userId, Integer questionCount) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new CustomApiException(INTERVIEW_NOT_FOUND.getHttpStatus(), INTERVIEW_NOT_FOUND, INTERVIEW_NOT_FOUND.getMessage()));

        // 소유자 검증
        if (interview.getUserId().equals(userId)) {
        } else {
            throw new CustomApiException(INTERVIEW_FORBIDDEN.getHttpStatus(), INTERVIEW_FORBIDDEN, INTERVIEW_FORBIDDEN.getMessage());
        }

        // 중복 생성 방지: 정책 1) 이미 생성되었으면 막는다
        if (interviewQuestionRepository.existsByInterviewId(interviewId)) {
            throw new CustomApiException(QUESTIONS_ALREADY_GENERATED.getHttpStatus(), QUESTIONS_ALREADY_GENERATED, QUESTIONS_ALREADY_GENERATED.getMessage());
        }

        int n = (questionCount == null || questionCount <= 0) ? 5 : questionCount;

        GenerateQuestionCommand cmd = new GenerateQuestionCommand(
                userId,
                interview.getTrack(),
                interview.getDifficulty(),
                interview.getInterviewMode(),
                interview.getAnswerTimeSeconds(),
                n
        );

        List<GeneratedQuestion> generated = generator.generate(cmd);
        OffsetDateTime now = OffsetDateTime.now();

        List<InterviewQuestion> entities = generated.stream()
                .map(g -> InterviewQuestion.create(
                        interview,
                        g.seq(),
                        g.questionText(),
                        g.provider(),
                        g.model(),
                        g.promptVersion(),
                        g.temperature(),
                        now
                ))
                .toList();

        return InterviewQuestionMapper.fromList(interviewQuestionRepository.saveAll(entities));

    }

    @Transactional(readOnly = true)
    public InterviewQuestionReadResponse getQuestions(Long interviewId) {
        return InterviewQuestionMapper.fromList(interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interviewId));
    }
}
