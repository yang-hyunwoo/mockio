package com.mockio.core_service.interview.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.response.InterviewUserInterviewSettingReadResponse;

public class UserInterviewSettingMapper {

    public static InterviewUserInterviewSettingReadResponse from(UserInterviewSetting userInterviewSetting) {
        return new InterviewUserInterviewSettingReadResponse(
                userInterviewSetting.getId(),
                EnumResponse.of(
                        userInterviewSetting.getTrack().name(),
                        userInterviewSetting.getTrack().getLabel()
                ),
                EnumResponse.of(
                        userInterviewSetting.getDifficulty().name(),
                        userInterviewSetting.getDifficulty().getLabel()
                ),
                EnumResponse.of(
                        userInterviewSetting.getFeedbackStyle().name(),
                        userInterviewSetting.getFeedbackStyle().getLabel()
                ),
                EnumResponse.of(
                        userInterviewSetting.getInterviewMode().name(),
                        userInterviewSetting.getInterviewMode().getLabel()
                ),
                userInterviewSetting.getAnswerTimeSeconds(),
                userInterviewSetting.getInterviewQuestionCount()
        );
    }

}
