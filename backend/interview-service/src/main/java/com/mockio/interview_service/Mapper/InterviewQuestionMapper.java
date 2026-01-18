package com.mockio.interview_service.Mapper;

import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;

import java.util.List;

import static com.mockio.interview_service.dto.response.InterviewQuestionReadResponse.*;

public class InterviewQuestionMapper {

    private InterviewQuestionMapper() {
    }

    /** 단일 엔티티 → Item */
    public static Item from(InterviewQuestion question) {
        return new Item(
                question.getId(),
                question.getSeq(),
                question.getQuestionText()
        );
    }

    /** 엔티티 리스트 → Response */
    public static InterviewQuestionReadResponse fromList(List<InterviewQuestion> questions) {
        return new InterviewQuestionReadResponse(
                questions.stream()
                        .map(InterviewQuestionMapper::from)
                        .toList()
        );
    }
}
