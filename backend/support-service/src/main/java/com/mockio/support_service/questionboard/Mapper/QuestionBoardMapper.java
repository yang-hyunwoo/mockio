package com.mockio.support_service.questionboard.Mapper;

import com.mockio.support_service.questionboard.dto.internal.response.InterviewListResponse;
import com.mockio.support_service.questionboard.dto.internal.response.QuestionAnswerResponse;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardListResponse;

import java.util.List;

public class QuestionBoardMapper {

    public static QuestionBoardListResponse fromQuestionBoardList(Long selectedInterviewId,
                                                                  InterviewListResponse interviewList,
                                                                  QuestionAnswerResponse internalQuestionAnswerResponse,
                                                                  Long selectedQuestionId) {

        //면접 목록
        List<QuestionBoardListResponse.InterviewItem> interviewItemList = interviewList.interviews().stream()
                .map(item -> new QuestionBoardListResponse.InterviewItem(
                                item.id(),
                                item.title(),
                                item.createdAt()
                        )
                ).toList();

        //면접 질문 리스트
        List<QuestionBoardListResponse.QuestionAnswerItem> questionAnswerItem = internalQuestionAnswerResponse.questions().stream()
                .map(item -> new QuestionBoardListResponse.QuestionAnswerItem(
                        item.questionOrder(),
                        item.id(),
                        item.question(),
                        item.answer(),
                        item.score(),
                        item.feedback()
                )).toList();

        return new QuestionBoardListResponse(
                interviewItemList,
                selectedInterviewId,
                selectedQuestionId,
                questionAnswerItem
        );
    }

}


