package com.mockio.common_ai_contractor.generator.question;

/**
 * 질문 생성 DTO
 */

import java.util.List;
import java.util.Set;

public record GeneratedQuestion(
       List<Item> questions
) {
    public record Item(

            //seq
            int seq,

            //제목
            String title,

            //응답값
            String body,

            //중요 태그
            String primaryTag,

            //태그
            Set<String> tags,

            //ai
            String provider,

            //ai 모델
            String model,

            //ai 버전
            String promptVersion,

            //연관도
            Double temperature
    ) {}
}