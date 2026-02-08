package com.mockio.inquiry_service.dto.response;

import com.mockio.inquiry_service.constant.InquiryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class InquiryListResDto {

    @Schema(description = "InquiryId",example = "1")
    private Long id;

    @Schema(description = "질문 제목" ,example = "출석은 어떻게")
    private String questionTitle;

    @Schema(description = "질문 내용" ,example = "출석은 어떻게")
    private String questionContent;

    @Schema(description = "답변" ,example = "출석은 어떻게")
    private String answer;

    @Schema(description = "등록일" ,example = "출석은 어떻게")
    private OffsetDateTime createdAt;

    @Schema(description = "답변일" ,example = "출석은 어떻게")
    private OffsetDateTime answerAt;

    @Schema(description = "qna유형" , example = "SERVICE")
    private InquiryType qnaType;




}
