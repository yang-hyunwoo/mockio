package com.mockio.ai_service.openAi.generator;

import com.mockio.ai_service.openAi.client.OpenAIClient;
import com.mockio.ai_service.util.JsonSupport;
import com.mockio.common_ai_contractor.dto.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.dto.FollowUpQuestionResult;
import com.mockio.common_ai_contractor.generator.FollowUpQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@ConditionalOnProperty(name="ai.generator" , havingValue = "openai")
@RequiredArgsConstructor
public class OpenAIFollowUpQuestionGenerator implements FollowUpQuestionGenerator {

    private static final String MODEL = "gpt-4o-mini";
    private static final String PROMPT_VERSION = "followup-v1";

    private final OpenAIClient client;


    @Override
    public FollowUpQuestionResult generate(FollowUpQuestionCommand command) {
        String prompt = buildPrompt(command);

        String raw = client.chat(MODEL, prompt);

        // 1) JSON 파싱 시도
        FollowUpQuestionResult parsed = JsonSupport.tryParseFollowUp(raw);
        if (isUsable(parsed)) {
            return normalize(parsed);
        }

        // 2) 실패하면 1회 리페어(“JSON만 출력” 재요청)
        String repairPrompt = buildRepairPrompt(raw);
        String repairedRaw = client.chat(MODEL, repairPrompt);

        FollowUpQuestionResult repaired = JsonSupport.tryParseFollowUp(repairedRaw);
        if (isUsable(repaired)) {
            return normalize(repaired);
        }

        // 3) 최후 폴백(고정 질문) - 운영 정책에 따라 예외로 바꿔도 됩니다.
        return new FollowUpQuestionResult(
                "방금 답변에서 가장 핵심 근거를 한 가지로 요약하고, 그 근거가 깨지는 반례를 하나 들어보세요.",
                "검증",
                List.of("fallback", "clarify", "reasoning")
        );
    }

    private String buildPrompt(FollowUpQuestionCommand cmd) {
        List<FollowUpQuestionCommand.QAPair> qa = safeRecent3(cmd.recentQa());

        String recentBlock = IntStream.range(0, qa.size())
                .mapToObj(i -> {
                    var p = qa.get(i);
                    return """
                    %d) Q: %s
                       A: %s
                    """.formatted(i + 1, p.question(), trimAnswer(p.answer()));
                })
                .reduce("", String::concat);

        return """
        당신은 %s 기술면접관입니다.
        아래 최근 3개의 문답(최신이 마지막)을 바탕으로, 다음에 이어질 '후속 질문' 1개를 만들어 주세요.

        면접 설정:
        - 트랙: %s
        - 난이도: %s
        - 피드백 스타일: %s
        - promptVersion: %s

        규칙:
        - 질문은 1~2문장 이내
        - “더 설명해보세요” 같은 일반 질문 금지
        - 직전 답변의 모호한 지점, 핵심 주장, 빠진 케이스, 트레이드오프를 파고들 것
        - 이미 물어본 내용의 반복 금지
        - 출력은 반드시 JSON 하나만 (마크다운/설명/추가 텍스트 금지)

        출력 JSON 스키마:
        {
          "question": "string",
          "intent": "검증|구체화|반례|트레이드오프|경험",
          "tags": ["string", "string"]
        }

        최근 문답:
        %s
        """.formatted(
                nullToNA(cmd.track()),
                nullToNA(cmd.track()),
                nullToNA(cmd.difficulty()),
                nullToNA(cmd.feedbackStyle()),
                PROMPT_VERSION,
                recentBlock
        );
    }

    private String buildRepairPrompt(String raw) {
        return """
        아래 텍스트는 JSON만 출력해야 하는데 형식이 깨졌습니다.
        반드시 아래 스키마를 만족하는 JSON 객체 1개만 출력하세요. 다른 문장/설명/마크다운 금지.

        스키마:
        {
          "question": "string",
          "intent": "검증|구체화|반례|트레이드오프|경험",
          "tags": ["string", "string"]
        }

        원문:
        %s
        """.formatted(raw);
    }

    private boolean isUsable(FollowUpQuestionResult r) {
        if (r == null) return false;
        if (r.question() == null || r.question().isBlank()) return false;
        if (r.intent() == null || r.intent().isBlank()) return false;
        return r.tags() != null && !r.tags().isEmpty();
    }

    private FollowUpQuestionResult normalize(FollowUpQuestionResult r) {
        String q = r.question().trim();
        if (!q.endsWith("?") && !q.endsWith("요.") && !q.endsWith(".")) {
            q = q + "?";
        }
        List<String> tags = r.tags().stream()
                .filter(t -> t != null && !t.isBlank())
                .map(String::trim)
                .distinct()
                .limit(5)
                .toList();

        return new FollowUpQuestionResult(q, r.intent().trim(), tags);
    }

    private List<FollowUpQuestionCommand.QAPair> safeRecent3(List<FollowUpQuestionCommand.QAPair> in) {
        if (in == null || in.isEmpty()) {
            return List.of(new FollowUpQuestionCommand.QAPair("자기소개를 해주세요.", "저는 ..."));
        }
        return in.size() <= 3 ? in : in.subList(in.size() - 3, in.size());
    }

    private String trimAnswer(String answer) {
        if (answer == null) return "";
        String a = answer.trim();
        int max = 800;
        return (a.length() > max) ? a.substring(0, max) + " ...(truncated)" : a;
    }

    private String nullToNA(String v) {
        return (v == null || v.isBlank()) ? "N/A" : v.trim();
    }
}
