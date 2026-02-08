package com.mockio.faq_service.controller;

import com.mockio.common_jpa.dto.PageDto;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.faq_service.dto.request.FaqReqDto;
import com.mockio.faq_service.dto.response.FaqResDto;
import com.mockio.faq_service.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faq/v1/public")
@RequiredArgsConstructor
@Tag(name="FAQ 관리 API" ,description = "FaqController")
@Slf4j
public class FaqController {

    private final FaqService faqService;

    private final MessageUtil messageUtil;

    @Operation(summary = "Faq 리스트 조회", description = "Faq 리스트 조회")
    @GetMapping("/faq")
    public ResponseEntity<Response<PageDto<FaqResDto>>> faqList(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            FaqReqDto faqReqDto
    ) {
        return Response.ok(messageUtil.getMessage("response.read"),
                faqService.findFaqListPage(pageable, faqReqDto));
    }

}
