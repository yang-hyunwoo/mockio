package com.mockio.ai_service.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.SummaryFeedbackGenerator;
import org.springframework.stereotype.Component;

@Component
public class FakeSummaryFeedbackGenerator implements SummaryFeedbackGenerator {
    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command) {
        return new GeneratedSummaryFeedback(
            1L,
                "{\n" +
                        "  \"score\": 40,\n" +
                        "  \"summaryText\": \"지원자의 답변은 갈등 해결에 대한 기본적인 접근을 제시하지만, 구체적인 방법이나 사례가 부족하여 실무에서의 적용 가능성이 낮습니다.\",\n" +
                        "  \"strengths\": [],\n" +
                        "  \"improvements\": [\n" +
                        "    \"갈등 해결을 위한 구체적인 방법론이나 접근 방식을 제시해야 합니다.\",\n" +
                        "    \"실제 사례를 통해 어떻게 갈등을 해결했는지 설명하는 것이 필요합니다.\",\n" +
                        "    \"합의점 도달을 위한 구체적인 커뮤니케이션 전략을 언급해야 합니다.\"\n" +
                        "  ],\n" +
                        "}",
                50,
                "Fake",
                "FAKE",
                "FAKE",
                0.0
        );
    }

}
