package com.mockio.faq_service.service;

import com.mockio.faq_service.Mapper.FaqMapper;
import com.mockio.faq_service.dto.request.FaqReqDto;
import com.mockio.faq_service.dto.response.FaqResDto;
import com.mockio.faq_service.repository.FaqQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {

    private final FaqQueryDslRepository faqQueryDslRepository;

    /**
     * FAQ  조회
     * @param faqReqDto
     * @return faq 페이지 객체
     */
    @Transactional(readOnly = true)
    public FaqResDto findFaqList(FaqReqDto faqReqDto) {

        return FaqMapper.fromList(faqQueryDslRepository.findFaqList(faqReqDto));
    }

}
