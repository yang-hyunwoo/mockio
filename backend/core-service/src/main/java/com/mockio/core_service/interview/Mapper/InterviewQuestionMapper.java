package com.mockio.core_service.interview.Mapper;

import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;

import java.util.List;

public class InterviewQuestionMapper {

    private InterviewQuestionMapper() {
    }

    /** 단일 엔티티 → Item */
    public static InterviewQuestionReadResponse.Item from(InterviewQuestion question) {
        return new InterviewQuestionReadResponse.Item(
                question.getId(),
                question.getInterview().getId(),
                question.getSeq(),
                question.getTitle(),
                question.getQuestionText(),
                question.getTags(),
                EnumResponse.of(
                        question.getType().name(),
                        question.getType().getLabel()
                )
        );
    }

    /** 엔티티 리스트 → Response */
    public static InterviewQuestionReadResponse fromList(List<InterviewQuestion> questions, boolean completed , Interview interview) {
        return new InterviewQuestionReadResponse(
                questions.stream()
                        .map(InterviewQuestionMapper::from)
                        .toList(),
                interview.getId(),
                completed,
                interview.getAnswerTimeSeconds(),
                EnumResponse.of(
                        interview.getInterviewMode().name(),
                        interview.getInterviewMode().getLabel()
                )
        );
    }

}
