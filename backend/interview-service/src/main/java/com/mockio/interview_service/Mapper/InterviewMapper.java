package com.mockio.interview_service.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.domain.UserInterviewSetting;
import com.mockio.interview_service.dto.response.InterviewListResponse;
import com.mockio.interview_service.dto.response.InterviewQuestionReadResponse;
import com.mockio.interview_service.dto.response.UserInterviewSettingReadResponse;

import java.util.List;

import static com.mockio.interview_service.dto.response.InterviewListResponse.*;

public class InterviewMapper {

    public static Item from(Interview interview) {
        int total = interview.getTotalCount();
        int answered = interview.getAnsweredQuestions();

        int progress = (total == 0)
                ? 0
                : (int) Math.floor((answered * 100.0) / total);

        return new Item(
                interview.getId(),
                interview.getTrack().getLabel() + " (" + interview.getDifficulty().getLabel() + ")",
                interview.getCreatedAt(),
                progress
        );
    }

    /** 엔티티 리스트 → Response */
    public static InterviewListResponse fromList(List<Interview> interviews) {
        return new InterviewListResponse(
                interviews.stream()
                        .map(InterviewMapper::from)
                        .toList()
        );
    }
}
