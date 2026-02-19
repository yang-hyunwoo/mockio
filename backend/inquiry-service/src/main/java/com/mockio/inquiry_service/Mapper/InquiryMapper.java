package com.mockio.inquiry_service.Mapper;

import com.mockio.common_jpa.domain.vo.Content;
import com.mockio.inquiry_service.constant.InquiryType;
import com.mockio.inquiry_service.domain.InquiryBoard;
import com.mockio.inquiry_service.dto.request.InquiryWriteReqDto;
import com.mockio.inquiry_service.dto.response.InquiryListResDto;

public class InquiryMapper {

    public static InquiryBoard toEntity(String userId , InquiryWriteReqDto inquiryWriteReqDto) {
        return InquiryBoard.createInquiryBoard(
                userId,
                inquiryWriteReqDto.getQuestionTitle(),
                Content.notRequired(inquiryWriteReqDto.getQuestionContent()),
                null,
                null,
                InquiryType.valueOf(inquiryWriteReqDto.getQnaType())
        );
    }

    public static InquiryListResDto from(InquiryBoard inquiryBoard) {
        return InquiryListResDto.builder()
                .id(inquiryBoard.getId())
                .questionTitle(inquiryBoard.getQuestionTitle())
                .questionContent(inquiryBoard.getQuestionContent().getValue())
                .answer(inquiryBoard.getAnswerContent().getValue() != null ? inquiryBoard.getAnswerContent().getValue() : null)
                .createdAt(inquiryBoard.getCreatedAt())
                .answerAt(inquiryBoard.getAnswerAt())
                .qnaType(inquiryBoard.getInquiryType())
                .build();
    }

}
