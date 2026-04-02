package com.mockio.common_ai_contractor.generator.deepdive;

/**
 * Deep Dive 생성 결과를 담는 불변 번들 객체.
 *
 * Deep Dive 수행 여부({DeepDiveDecision})와
 * 생성된 후속 질문({FollowUpQuestion})을 함께 반환하기 위해 사용된다.
 *
 * DeepDiveGenerator의 실행 결과를 하나의 단위로 전달하기 위한 DTO이다.
 */

import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;

public record GeneratedDeepDiveBundle(
        DeepDiveDecision decision,
        FollowUpQuestion question
) {}