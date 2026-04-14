package com.mockio.core_service.ai.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.compare.CompareSummaryGenerator;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummary;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummaryCommand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FakeCompareSummaryGenerator implements CompareSummaryGenerator {
    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public GeneratedCompareSummary generate(GeneratedCompareSummaryCommand command) {
        return new GeneratedCompareSummary(
            "문구 부적절",
                "문장 부적절",
                List.of("강점"),
                List.of("약점"),
                List.of("부적절"),
                "Fake",
                "Fake",
                "v0.0",
                0.0
        );
    }

}
