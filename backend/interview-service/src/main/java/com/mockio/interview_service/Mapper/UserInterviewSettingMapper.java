package com.mockio.interview_service.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.interview_service.domain.UserInterviewSetting;
import com.mockio.interview_service.dto.response.UserInterviewSettingReadResponse;

public class UserInterviewSettingMapper {

    public static UserInterviewSettingReadResponse from(UserInterviewSetting userInterviewSetting) {
        return new UserInterviewSettingReadResponse(
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
                userInterviewSetting.getAnswerTimeSeconds()
        );
    }
}
