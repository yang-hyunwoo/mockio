package com.mockio.user_service.Mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.user_service.domain.UserInterviewPreference;
import com.mockio.user_service.dto.response.UserInterviewPreferenceReadResponse;

public class UserInterviewPreferenceMapper {

    public static UserInterviewPreferenceReadResponse from(UserInterviewPreference userInterviewPreference) {
        return new UserInterviewPreferenceReadResponse(
                userInterviewPreference.getId(),
                EnumResponse.of(
                        userInterviewPreference.getTrack().name(),
                        userInterviewPreference.getTrack().getLabel()
                ),
                EnumResponse.of(
                        userInterviewPreference.getDifficulty().name(),
                        userInterviewPreference.getDifficulty().getLabel()
                ),
                EnumResponse.of(
                        userInterviewPreference.getFeedbackStyle().name(),
                        userInterviewPreference.getFeedbackStyle().getLabel()
                ),
                EnumResponse.of(
                        userInterviewPreference.getInterviewMode().name(),
                        userInterviewPreference.getInterviewMode().getLabel()
                ),
                userInterviewPreference.getAnswerTimeSeconds()
        );
    }
}
