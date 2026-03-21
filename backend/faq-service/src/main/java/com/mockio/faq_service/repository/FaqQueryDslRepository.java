package com.mockio.faq_service.repository;

import com.mockio.faq_service.domain.FaqBoard;
import com.mockio.faq_service.dto.request.FaqReqDto;

import java.util.List;

public interface FaqQueryDslRepository {

    /**
     * faq 목록 페이징 조회
     *
     * @param faqReqDto 검색 조건
     * @return faq 목록 페이지 객체
     */
    List<FaqBoard> findFaqList(FaqReqDto faqReqDto);

}
