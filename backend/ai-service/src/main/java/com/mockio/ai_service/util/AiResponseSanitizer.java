package com.mockio.ai_service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiResponseSanitizer {

    private final ObjectMapper objectMapper;

    public Integer extractScoreSafely(String rawJson,String fieldName) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            JsonNode node = root.get(fieldName);
            if (node == null || node.isNull()) return null;

            int score;
            if (node.isInt()) score = node.asInt();
            else if (node.isNumber()) score = (int) Math.round(node.asDouble());
            else if (node.isTextual()) {
                String digits = node.asText().replaceAll("[^0-9]", "");
                if (digits.isBlank()) return null;
                score = Integer.parseInt(digits);
            } else return null;

            return (score >= 0 && score <= 100) ? score : null;
        } catch (Exception e) {
            log.warn("score extraction failed. raw={}", truncate(rawJson), e);
            return null;
        }
    }

    public String truncate(String s) {
        if (s == null) return "";
        return s.length() > 500 ? s.substring(0, 500) + "...(truncated)" : s;
    }

    public String normalizeQuestionLine(String line) {
        String q = line == null ? "" : line.trim();
        q = q.replaceAll("^\"|\"$", "").trim();
        if (!q.endsWith("?") && !q.endsWith("요.") && !q.endsWith(".")) q = q + "?";
        return q;
    }

    public Set<String> sanitizeTags(Set<String> tags) {
        if (tags == null) return Set.of();

        return tags.stream()
                .map(String::trim)
                .filter(t -> !t.isBlank())
                .map(t -> t.length() > 20 ? t.substring(0, 20) : t)
                .distinct()
                .limit(4)
                .collect(toSet());
    }

    public String normalizeBody(String body) {
        if (body == null) return "";
        String q = body.trim();
        if (q.isBlank()) return q;
        if (!q.endsWith("?") && !q.endsWith("요.") && !q.endsWith(".")) q += "?";
        return q;
    }

}
