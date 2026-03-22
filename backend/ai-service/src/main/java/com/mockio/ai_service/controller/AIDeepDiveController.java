package com.mockio.ai_service.controller;

/**
 * AI DeepDive 관련 API를 제공하는 컨트롤러
 *
 * - 사용자의 입력을 기반으로 심층 질문(Deep Dive Question)을 생성
 * - 입력값 검증(validate) + 생성(generate) 로직을 동시에 수행
 *
 * Endpoint:
 *  POST /api/ai/v1/questions/deepdive/validate-and-generate
 */

import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveGenerator;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.GeneratedDeepDiveBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ai/v1/questions")
@RequiredArgsConstructor
public class AIDeepDiveController {

    private final DeepDiveGenerator deepDiveGenerator;

    /**
     * DeepDive 질문 생성 API
     *
     * [동작 흐름]
     * 1. 클라이언트로부터 GenerateDeepDiveCommand 요청 수신
     * 2. 입력값에 대한 검증 수행 (내부 로직)
     * 3. AI 기반 DeepDive 질문 생성
     * 4. 결과를 GeneratedDeepDiveBundle 형태로 반환
     *
     * @param command DeepDive 생성 요청 데이터 (질문, 컨텍스트 등 포함)
     * @return 생성된 DeepDive 결과 묶음 (질문 리스트, 메타데이터 등)
     */
    @PostMapping("/deepdive/validate-and-generate")
    public GeneratedDeepDiveBundle generate(@RequestBody GenerateDeepDiveCommand command) {
        return deepDiveGenerator.generate(command);
    }

}
