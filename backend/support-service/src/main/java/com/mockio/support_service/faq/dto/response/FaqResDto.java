package com.mockio.support_service.faq.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FaqResDto(

        @Schema(description = "faq 리스트" ,example = "[]")
        List<Item> items

) {
    public record Item(

            @Schema(description = "faq ID" ,example = "1")
            int id,

            @Schema(description = "질문" ,example = "출석은 어떻게")
            String question,

            @Schema(description = "답변",example = "출석은 이렇게 하면 되요")
            String answer,

            @Schema(description = "FAQ 유형", example = "ALL")
            EnumResponse faqType

    ) {}
}
