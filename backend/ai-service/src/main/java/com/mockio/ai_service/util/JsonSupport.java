package com.mockio.ai_service.util;

/**
 * LLM(OpenAI 등) 응답에서 JSON 객체를 관대하게 추출·파싱하기 위한 유틸리티 클래스.
 *
 * <p>LLM 응답에 설명 문구나 불필요한 텍스트가 섞여 있는 경우에도
 * 첫 번째 '{'와 마지막 '}' 사이의 JSON 객체를 추출하여
 * 지정된 DTO로 파싱을 시도한다.</p>
 *
 * <p>파싱 실패 시 예외를 던지지 않고 null을 반환하여,
 * 상위 계층에서 재요청(repair) 또는 폴백 전략을 선택할 수 있도록 한다.</p>
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.dto.FollowUpQuestionResult;

public final class JsonSupport {

    private static final ObjectMapper om = new ObjectMapper();

    private JsonSupport() {}

    /**
     * 원본 텍스트에서 FollowUpQuestionResult JSON 객체 파싱을 시도한다.
     *
     * @param raw LLM이 반환한 원본 응답 텍스트
     * @return 파싱 성공 시 FollowUpQuestionResult, 실패 시 null
     */
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
     * 문자열 내에서 JSON 객체로 보이는 부분을 추출한다.
     *
     * <p>첫 번째 '{'와 마지막 '}' 사이의 문자열을 JSON 객체로 간주한다.</p>
     *
     * @param raw 원본 문자열
     * @return JSON 객체 문자열, 추출 실패 시 null
     */
    private static String extractJsonObject(String raw) {
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
        if (start < 0 || end < 0 || end <= start) return null;
        return raw.substring(start, end + 1).trim();
    }
}
