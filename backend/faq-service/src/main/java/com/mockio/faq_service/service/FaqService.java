package com.mockio.faq_service.service;

import com.mockio.common_jpa.dto.PageDto;
import com.mockio.faq_service.Mapper.FaqMapper;
import com.mockio.faq_service.dto.request.FaqReqDto;
import com.mockio.faq_service.dto.response.FaqResDto;
import com.mockio.faq_service.repository.FaqQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqService {

    private final FaqQueryDslRepository faqQueryDslRepository;

    /**
     * FAQ 페이징 조회
     * @param pageable
     * @param faqReqDto
     * @return faq 페이지 객체
     */
    @Transactional(readOnly = true)
    public PageDto<FaqResDto> findFaqListPage(Pageable pageable,
                                              FaqReqDto faqReqDto
    ) {
        return PageDto.of(faqQueryDslRepository.findFaqListPage(pageable, faqReqDto), FaqMapper::from);
    }

}
