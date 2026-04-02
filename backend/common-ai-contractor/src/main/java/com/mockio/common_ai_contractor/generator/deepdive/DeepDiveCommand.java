package com.mockio.common_ai_contractor.generator.deepdive;

/**
 * 딥 다이브 ai 요청 DTO
 */


import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

public record DeepDiveCommand (

        /** 면접 트랙 **/
        InterviewTrack interviewTrack,

        /** 면접 난이도 **/
        InterviewDifficulty interviewDifficulty,

        /** 메인 질문 **/
        String basicQuestion,

        /** 메인 답변 **/
        String basicAnswer,

        /** 꼬리 질문 **/
        String question,

        /** 꼬리 질문 답변 **/
        String answer
) {}
