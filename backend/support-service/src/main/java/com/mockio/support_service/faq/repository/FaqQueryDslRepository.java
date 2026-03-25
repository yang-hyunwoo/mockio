package com.mockio.support_service.faq.repository;


import com.mockio.support_service.faq.domain.FaqBoard;
import com.mockio.support_service.faq.dto.request.FaqReqDto;

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
