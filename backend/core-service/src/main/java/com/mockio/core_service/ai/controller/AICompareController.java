package com.mockio.core_service.ai.controller;

import com.mockio.common_ai_contractor.generator.compare.*;
import com.mockio.core_service.ai.generator.CompositeCompareQuestionGenerator;
import com.mockio.core_service.ai.generator.CompositeCompareSummaryGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "AI")
@RestController
@RequestMapping("api/ai/v1/compare")
@RequiredArgsConstructor
public class AICompareController {

    private final CompositeCompareSummaryGenerator compareSummaryGenerator;
    private final CompositeCompareQuestionGenerator compositeCompareQuestionGenerator;


    @Operation(summary = "면접 비교 요약 생성")
    @PostMapping("/summary/generate")
    public GeneratedCompareSummary generateSummary(@RequestBody @Valid GeneratedCompareSummaryCommand command) {
        return compareSummaryGenerator.generate(command);
    }

    @Operation(summary = "면접 비교 질문 요약 생성")
    @PostMapping("/question/generate")
    public GeneratedCompareQuestion generateCompareQuestion(@RequestBody @Valid GeneratedCompareQuestionCommand command) {
        return compositeCompareQuestionGenerator.generate(command);
    }

}
