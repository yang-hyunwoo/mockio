package com.mockio.common_ai_contractor.generator.question;

/**
 * 질문 생성 DTO
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

import java.util.List;

public record GenerateQuestionCommand(

        //사용자 ID
        Long userId,

        //면접 트랙
        InterviewTrack track,

        //면접 난이도
        InterviewDifficulty difficulty,

        //면접 답변 모드
        InterviewMode interviewMode,

        //면접 응답 시간
        Integer answerTimeSeconds,

        //질문 갯수
        int questionCount,

        //중요 태그
        List<String> primaryTag
) {}
