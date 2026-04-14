package com.mockio.core_service.ai.controller;

/**
 * AI 피드백 생성 API 컨트롤러
 *
 * - 개별 질문 단위 피드백 생성
 * - 전체 인터뷰/응답에 대한 요약 피드백 생성
 *
 * Endpoint:
 *  POST /api/ai/v1/feedback/question  : 단일 질문 피드백
 *  POST /api/ai/v1/feedback/summary   : 전체 요약 피드백
 */

import com.mockio.common_ai_contractor.generator.feedback.*;
import com.mockio.core_service.ai.generator.CompositeFeedbackEvaluationGenerator;
import com.mockio.core_service.ai.generator.CompositeFeedbackGenerator;
import com.mockio.core_service.ai.generator.CompositeSummaryFeedbackGenerator;
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
@RequestMapping("api/ai/v1/feedback")
@RequiredArgsConstructor
public class AIFeedbackController {

    private final CompositeFeedbackGenerator compositeFeedbackGenerator;
    private final CompositeSummaryFeedbackGenerator compositeSummaryFeedbackGenerator;
    private final CompositeFeedbackEvaluationGenerator compositeFeedbackEvaluationGenerator;

    /**
     * 단일 질문 피드백 생성 API
     *
     * [동작 흐름]
     * 1. 클라이언트로부터 질문 + 답변 데이터 수신
     * 2. AI 기반 피드백 생성 로직 수행
     * 3. 결과를 GeneratedFeedback 형태로 반환
     *
     * @param command 피드백 생성 요청 (질문, 사용자 답변 등)
     * @return 생성된 개별 질문 피드백
     */
    @Operation(summary = "단일 피드백 생성")
    @PostMapping("/question")
    public GeneratedFeedback singleFeedback(@RequestBody @Valid GenerateFeedbackCommand command) {
        return compositeFeedbackGenerator.generate(command);
    }

    @PostMapping("/question/evaluation")
    public GeneratedFeedbackEvaluation singleFeedbackEvaluation(@RequestBody @Valid GenerateFeedbackCommand command) {
        return compositeFeedbackEvaluationGenerator.generate(command);
    }

    /**
     * 전체 요약 피드백 생성 API
     *
     * [동작 흐름]
     * 1. 여러 질문/답변 리스트 수신
     * 2. 전체 흐름을 기반으로 종합 평가 수행
     * 3. 강점, 보완점, 총평 등을 포함한 요약 피드백 반환
     *
     * @param command 요약 피드백 생성 요청 (전체 응답 데이터)
     * @return 생성된 요약 피드백 결과
     */
    @Operation(summary = "전체 요약 피드백 생성")
    @PostMapping("/summary")
    public GeneratedSummaryFeedback summaryFeedback(@RequestBody @Valid GeneratedSummaryFeedbackCommand command) {
        return compositeSummaryFeedbackGenerator.generate(command);
    }

}
