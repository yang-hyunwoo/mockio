package com.mockio.inquiry_service.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.inquiry_service.dto.request.InquiryWriteReqDto;
import com.mockio.inquiry_service.dto.response.InquiryListResDto;
import com.mockio.inquiry_service.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiry/v1")
@RequiredArgsConstructor
@Tag(name = "Inquiry 관리 API", description = "InquiryController")
@Slf4j
public class InquiryController {

    private final InquiryService inquiryService;

    private final MessageUtil messageUtil;

    @Operation(summary = "Inquiry 조회", description = "Inquiry 조회")
    @GetMapping("/contact/qna/list")
    public ResponseEntity<Response<List<InquiryListResDto>>> qnaList(
            @CurrentSubject String userId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                inquiryService.inquiryList(userId));
    }

    @Operation(summary = "Inquiry 등록", description = "Inquiry 등록")
    @PostMapping("/contact/qna/insert")
    public ResponseEntity<Response<Void>> qnaInsert(
            @RequestBody InquiryWriteReqDto inquiryWriteReqDto,
            @CurrentSubject String userId
    ) {
        inquiryService.inquirySave(inquiryWriteReqDto, userId);
        return Response.create(messageUtil.getMessage("response.create"),
                null);
    }

}
