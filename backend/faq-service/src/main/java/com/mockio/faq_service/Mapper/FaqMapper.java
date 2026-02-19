package com.mockio.faq_service.Mapper;

import com.mockio.faq_service.domain.FaqBoard;
import com.mockio.faq_service.dto.response.FaqResDto;

public class FaqMapper {

    public static FaqResDto from(FaqBoard faqBoard) {
        return FaqResDto.builder()
                .id(faqBoard.getId())
                .question(faqBoard.getQuestion().getValue())
                .answer(faqBoard.getAnswer().getValue())
                .faqType(faqBoard.getFaqType())
                .build();
    }

}
