package com.mockio.common_ai_contractor.generator.compare;

/**
 * 비교 면접 질문 요약 콘텐츠 생성을 위한 전략 인터페이스.
 *
 * 각 구현체는 특정 {AiEngine}에 대응하여
 * 비교 면접 질문 요약 결과({GeneratedCompareQuestion})를 생성한다.
 *
 * 엔진별로 생성 방식이 다르기 때문에,
 * Strategy 패턴을 통해 AI 엔진별 구현을 분리한다.
 *
 * 사용 시 {engine()} 값을 기준으로
 * 적절한 구현체를 선택하여 {generate(GeneratedCompareQuestionCommand)}를 호출한다.
 */

import com.mockio.common_ai_contractor.constant.AiEngine;

public interface CompareQuestionGenerator {
    AiEngine engine();
    GeneratedCompareQuestion generate(GeneratedCompareQuestionCommand command);
}
