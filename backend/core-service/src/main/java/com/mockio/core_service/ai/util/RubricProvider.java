package com.mockio.core_service.ai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.core_service.ai.openAi.dto.RubricItem;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

@Component
public class RubricProvider {

    private final Map<String, List<RubricItem>> rubricMap;

    public RubricProvider(ObjectMapper objectMapper) {
        try {
            InputStream is = new ClassPathResource("rubric/rubric.json").getInputStream();

            this.rubricMap = objectMapper.readValue(
                    is,
                    new TypeReference<Map<String, List<RubricItem>>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException("rubric.json 로딩 실패", e);
        }
    }

    public List<RubricItem> getByTrack(String track) {
        return rubricMap.getOrDefault(track, rubricMap.get("GENERAL"));
    }
}
