package com.mockio.ai_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.dto.FollowUpQuestionResult;

public final class JsonSupport {

    private static final ObjectMapper om = new ObjectMapper();

    private JsonSupport() {}

    public static FollowUpQuestionResult tryParseFollowUp(String raw) {
        if (raw == null || raw.isBlank()) return null;

        String json = extractJsonObject(raw);
        if (json == null) return null;

        try {
            return om.readValue(json, FollowUpQuestionResult.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 모델이 앞뒤로 설명을 섞어도 { ... } 구간만 뽑아 파싱 시도
     */
    private static String extractJsonObject(String raw) {
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
        if (start < 0 || end < 0 || end <= start) return null;
        return raw.substring(start, end + 1).trim();
    }
}
