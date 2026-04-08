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
          "improvements": ["질문의 핵심 요소를 먼저 정리해 답변해 주세요."],
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
