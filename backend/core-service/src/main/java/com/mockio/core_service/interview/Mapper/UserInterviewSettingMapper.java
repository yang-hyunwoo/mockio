package com.mockio.core_service.interview.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.core_service.interview.domain.InterviewKeyword;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.response.InterviewUserInterviewSettingReadResponse;

import java.util.List;

public class UserInterviewSettingMapper {


    public static InterviewUserInterviewSettingReadResponse from(UserInterviewSetting userInterviewSetting) {
        List<String> list = userInterviewSetting.getKeywords()
                .stream()
                .map(InterviewKeyword::getKeyword)
                .toList();

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
                userInterviewSetting.getInterviewQuestionCount(),
                list

        );
    }

}
