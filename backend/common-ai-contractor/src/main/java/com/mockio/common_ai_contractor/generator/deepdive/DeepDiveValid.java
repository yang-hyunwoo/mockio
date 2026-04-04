package com.mockio.common_ai_contractor.generator.deepdive;

/**
 * 딥 다이브 ai 생성 여부 DTO
 */


public record DeepDiveValid(

        //딥다이브 생성 여부 확인
        boolean shouldDeepDive,

        //이유
        String reason,

        //관점
        String focus
) {}
