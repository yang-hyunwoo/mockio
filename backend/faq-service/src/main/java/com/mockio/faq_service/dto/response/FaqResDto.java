package com.mockio.faq_service.dto.response;

import com.mockio.faq_service.constant.FaqType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FaqResDto {

    @Schema(description = "FaqPk",example = "1")
    private Long id;

    @Schema(description = "질문" ,example = "출석은 어떻게")
    private String question;

    @Schema(description = "답변",example = "출석은 이렇게 하면 되요")
    private String answer;

    @Schema(description = "FAQ 유형", example = "ALL")
    private FaqType faqType;




}
