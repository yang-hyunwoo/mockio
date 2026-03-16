package com.mockio.faq_service.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.faq_service.domain.FaqBoard;
import com.mockio.faq_service.dto.response.FaqResDto;


import java.util.List;

public class FaqMapper {

    public static FaqResDto.Item from(FaqBoard faqBoard) {
        return new FaqResDto.Item(
                faqBoard.getId().intValue(),
                faqBoard.getQuestion().getValue(),
                faqBoard.getAnswer().getValue(),
                EnumResponse.of(
                        faqBoard.getFaqType().name(),
                        faqBoard.getFaqType().getLabel()
                )
        );
    }

    public static FaqResDto fromList(List<FaqBoard> faqBoards) {
        return new FaqResDto(
                faqBoards.stream()
                        .map(FaqMapper::from)
                        .toList()
        );
    }
}
