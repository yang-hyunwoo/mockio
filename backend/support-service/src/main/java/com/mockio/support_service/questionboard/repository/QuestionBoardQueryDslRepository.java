package com.mockio.support_service.questionboard.repository;

import com.mockio.support_service.questionboard.dto.request.QuestionBoardListRequest;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardDslListResponse;
import org.springframework.data.domain.Page;


public interface QuestionBoardQueryDslRepository {

    /**
     * 면접 공유 게시판 목록 페이징 조회
     *
     * @param req 검색 조건
     * @return QuestionBoardDslListResponse 목록 페이지 객체
     */
    Page<QuestionBoardDslListResponse> findQuestionBoardList(QuestionBoardListRequest req);

}
