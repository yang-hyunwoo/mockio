package com.mockio.core_service.ai.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackGenerator;
import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import org.springframework.stereotype.Component;

@Component
public class FakeFeedBackGenerator implements FeedbackGenerator {

    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }


    @Override
    public GeneratedFeedback generate(GenerateFeedbackCommand command) {
        String fakeJson = """
        {
          "score": 30,
          "dimensions": {
            "structure": 20,
            "clarity": 25,
            "specificity": 15
          },
          "headline": "핵심 내용이 부족합니다.",
          "summary": "답변이 질문 의도를 충분히 충족하지 못했습니다.",
          "strengths": [],
           "improvements": [
                          {
                              "problem": "사례가 구체적이지 않아 실무 적용의 신뢰성이 떨어집니다.",
                              "action": "실제 프로젝트에서 캐싱을 적용한 구체적인 상황을 추가하여 설명하세요.",
                              "example": "예: '게시글 조회 API에서 Redis를 사용하여 응답 시간을 30% 단축시켰습니다.'"
                          }
                      ],
          "improvementTags": ["핵심 답변 부족"],
          "modelAnswer": "질문의 핵심 요구사항을 먼저 파악한 뒤, 근거와 사례를 포함해 구조적으로 설명하겠습니다."
        }
        """;

        return new GeneratedFeedback(
                fakeJson,
                30,
                "FAKE",
                "FAKE",
                "0.0",
                0.0
        );
    }
}
