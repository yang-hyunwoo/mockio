package com.mockio.inquiry_service.service;

import com.mockio.inquiry_service.Mapper.InquiryMapper;
import com.mockio.inquiry_service.domain.InquiryBoard;
import com.mockio.inquiry_service.dto.request.InquiryWriteReqDto;
import com.mockio.inquiry_service.dto.response.InquiryListResDto;
import com.mockio.inquiry_service.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    /**
     * qna 저장
     * @param inquiryWriteReqDto
     * @param userId
     * @return
     */
    public Long inquirySave(InquiryWriteReqDto inquiryWriteReqDto,String userId) {
        return inquiryRepository.save(InquiryMapper.toEntity(userId,inquiryWriteReqDto)).getId();
    }

    /**
     * qna 리스트 조회
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<InquiryListResDto> inquiryList(String userId) {
        List<InquiryBoard> byQuestionUserIdNative = inquiryRepository.findByQuestionUserIdNative(userId);
        return byQuestionUserIdNative.stream().map(InquiryMapper::from).toList();
    }

}
