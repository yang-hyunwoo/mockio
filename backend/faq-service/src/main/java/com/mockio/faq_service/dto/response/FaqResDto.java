package com.mockio.faq_service.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.faq_service.constant.FaqType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

public record FaqResDto(
        List<Item> items
) {
    public record Item(
            int id,
            @Schema(description = "질문" ,example = "출석은 어떻게")
            String question,
            @Schema(description = "답변",example = "출석은 이렇게 하면 되요")
            String answer,
            @Schema(description = "FAQ 유형", example = "ALL")
            EnumResponse faqType
    ) {}
}
