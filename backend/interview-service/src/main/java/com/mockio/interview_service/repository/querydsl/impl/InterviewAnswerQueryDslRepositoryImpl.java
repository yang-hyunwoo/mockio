package com.mockio.interview_service.repository.querydsl.impl;

import com.mockio.interview_service.domain.QInterview;
import com.mockio.interview_service.domain.QInterviewAnswer;
import com.mockio.interview_service.domain.QInterviewQuestion;
import com.mockio.interview_service.dto.response.InterviewQuestionAnswerDetailResponse;
import com.mockio.interview_service.dto.response.QInterviewQuestionAnswerDetailResponse;
import com.mockio.interview_service.repository.querydsl.InterviewAnswerQueryDslRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class InterviewAnswerQueryDslRepositoryImpl implements InterviewAnswerQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    QInterview interview = QInterview.interview;
    QInterviewQuestion question = QInterviewQuestion.interviewQuestion;
    QInterviewAnswer answer = QInterviewAnswer.interviewAnswer;

    @Override
    public Optional<InterviewQuestionAnswerDetailResponse> interviewAnswerDetail(Long userId, Long questionId) {
        BooleanBuilder builder = new BooleanBuilder();
        answerCondition(userId, questionId, builder);

        InterviewQuestionAnswerDetailResponse interviewQuestionAnswerDetailResponse = queryFactory.select(new QInterviewQuestionAnswerDetailResponse(
                        answer.id,
                        question.id,
                        answer.answerText,
                        answer.idempotencyKey,
                        answer.answerDurationSeconds
                ))
                .from(interview)
                .innerJoin(question)
                .on(interview.id.eq(question.interview.id))
                .innerJoin(answer)
                .on(question.id.eq(answer.question.id))
                .where(builder)
                .fetchOne();
        return Optional.ofNullable(interviewQuestionAnswerDetailResponse);
    }

    private void answerCondition(Long userId,
                                 Long questionId,
                                 BooleanBuilder builder) {
        builder.and(interview.userId.eq(userId));
        builder.and(question.id.eq(questionId));
    }

}
