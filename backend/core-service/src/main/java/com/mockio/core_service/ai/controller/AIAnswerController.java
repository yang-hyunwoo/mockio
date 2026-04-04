package com.mockio.core_service.ai.controller;

import com.mockio.common_ai_contractor.generator.question.GenerateSttCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedStt;
import com.mockio.common_ai_contractor.generator.question.SttGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "AI",
        description = """
                AI 관련 API 입니다.
                
                - 텍스트 변환
                - 딥다이브 질문 생성 체크 
                - 딥다이브 질문 생성
                - 단일 질문 피드백 생성
                - 전체 요약 피드백 생성
                - 질문 생성
                - 꼬리질문 생성 
                - 꼬리질문 생성 체크
                """
)
@RestController
@RequestMapping("api/ai/v1/answer")
@RequiredArgsConstructor
public class AIAnswerController {

    private final SttGenerator sttGenerator;

    @Operation(summary = "텍스트 변환")
    @PostMapping(value = "/stt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GeneratedStt generateStt(@RequestParam("file") @Parameter(description = "파일", example = "file") MultipartFile file) throws IOException {
        GenerateSttCommand command = new GenerateSttCommand(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );
       return sttGenerator.generate(command);
    }

}
