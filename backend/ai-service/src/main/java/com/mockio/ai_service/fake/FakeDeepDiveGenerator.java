package com.mockio.ai_service.fake;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveDecision;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveGenerator;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.GeneratedDeepDiveBundle;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class FakeDeepDiveGenerator implements DeepDiveGenerator {
    @Override
    public AiEngine engine() {
        return AiEngine.FAKE;
    }

    @Override
    public GeneratedDeepDiveBundle generate(GenerateDeepDiveCommand command) {
        return new GeneratedDeepDiveBundle(
                new DeepDiveDecision(
                        true,
                        1,
                        List.of(),
                        List.of(),
                        "이유"
                ),
                new FollowUpQuestion(
                        new FollowUpQuestion.Item(
                                "딥 다이브",
                                "딥 다이브 질문",
                                Set.of("딥다이브"),
                                "FAKE",
                                "FAKE",
                                "0.0",
                                0.0
                        )
                )
        );

    }
}
