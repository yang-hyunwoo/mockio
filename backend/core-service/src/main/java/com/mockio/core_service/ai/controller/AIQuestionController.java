package com.mockio.core_service.ai.controller;

/**
 * AI 기반 인터뷰 질문 생성 및 후속 질문 생성을 위한 API 컨트롤러.
 *
 * 클라이언트로부터 질문 생성 요청을 받아
 * InterviewQuestionGenerator 및 FollowUpQuestionGenerator를 통해
 * 질문을 생성하고 결과를 반환한다.
 *
 * 비즈니스 로직은 포함하지 않으며,
 * 요청/응답을 중계하는 역할에 집중한다.
 *
 * 주요 기능:
 *  기본 인터뷰 질문 생성
 *  이전 답변을 기반으로 한 후속 질문 생성
 */

import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.question.InterviewQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ai/v1/questions")
@RequiredArgsConstructor
public class AIQuestionController {

    private final InterviewQuestionGenerator questionGenerator;
    private final FollowUpQuestionGenerator followUpQuestionGenerator;

    /**
     * 인터뷰 질문 생성 API
     *
     * 클라이언트로부터 질문 생성 요청 수신
     * 입력 조건 기반으로 AI 질문 생성
     * 생성된 질문을 반환
     *
     * @param command 질문 생성 요청 데이터 (직무, 난이도, 키워드 등)
     * @return 생성된 인터뷰 질문
     */
    @PostMapping("/generate")
    public GeneratedQuestion generate(@RequestBody GenerateQuestionCommand command) {
        return questionGenerator.generate(command);
    }

    /**
     * 후속 질문 생성 API
     *
     *  이전 질문 및 사용자 답변 수신
     *  답변 내용을 기반으로 심화 질문 생성
     *  생성된 후속 질문 반환
     *
     * @param command 후속 질문 생성 요청 데이터 (이전 질문, 답변 등)
     * @return 생성된 후속 질문
     */
    @PostMapping("/followup")
    public FollowUpQuestion followup(@RequestBody FollowUpQuestionCommand command) {
        return followUpQuestionGenerator.generate(command);
    }

}
