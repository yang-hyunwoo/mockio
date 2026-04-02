package com.mockio.common_ai_contractor.generator.followup;

/**
 * follow up 질문 생성 DTO
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

public record FollowUpQuestionCommand(

        //면접 트랙
        InterviewTrack interviewTrack,

        //면접 난이도
        InterviewDifficulty interviewDifficulty,

        //면접 피드백 스타일
        InterviewFeedbackStyle feedbackStyle,      // 친절/압박

        //이유
        String followUpReason,              //꼬리질문 이유
        QAPair recentQa
) {
    public record QAPair(String question, String answer) {}
}
