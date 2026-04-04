package com.mockio.common_ai_contractor.generator.question;

/**
 * ai 질문 DTO
 */

import java.util.Set;

public record AiQuestion(

        //제목
        String title,

        //응답 값
        String body,

        //중요 key
        String primaryTag,

        //태그
        Set<String> tags
) {}