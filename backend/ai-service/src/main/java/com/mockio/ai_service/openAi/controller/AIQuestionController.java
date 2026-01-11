package com.mockio.ai_service.openAi.controller;

/**
 * AI 기반 인터뷰 질문 생성을 위한 API 컨트롤러.
 *
 * <p>클라이언트로부터 질문 생성 요청을 받아
 * InterviewQuestionGenerator를 통해 질문을 생성하고
 * 결과를 그대로 반환한다.</p>
 *
 * <p>비즈니스 로직은 포함하지 않으며,
 * 요청/응답 중계 역할에 집중한다.</p>
 */

import com.mockio.common_ai_contractor.generator.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ai/v1/questions")
@RequiredArgsConstructor
public class AIQuestionController {

    private final InterviewQuestionGenerator questionGenerator;

    @PostMapping("/generate")
    public GeneratedQuestion generate(@RequestBody GenerateQuestionCommand command) {
        return questionGenerator.generate(command);
    }

}
