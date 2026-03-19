package com.mockio.user_service.mapper;

/**
 * UserProfileMapper
 *
 * DTO / Domain 변환  Mapper.
 *
 */

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.response.*;

public class UserProfileMapper {

    public static UserProfileResponse from(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.getId(),
                userProfile.getNickname(),
                userProfile.getName(),
                userProfile.getEmail(),
                userProfile.getProfileImageId(),
                null,
                userProfile.getBio(),
                EnumResponse.of(
                        userProfile.getVisibility().name(),
                        userProfile.getVisibility().getLabel()
                ),
                EnumResponse.of(
                        userProfile.getStatus().name(),
                        userProfile.getStatus().getLabel()
                ),
                userProfile.getLastLoginAt()
        );
    }

    public static UserProfileDetailResponse fromDetail(UserProfile userProfile,
                                                       UserInterviewSettingReadResponse userInterviewSettingReadResponse,
                                                       UserProfileImageResponse userProfileImageResponse) {

        MyPageProfileDetailResponse myPageProfileDetailResponse = new MyPageProfileDetailResponse(
                userProfile.getNickname(),
                userProfile.getEmail(),
                userProfile.getProfileImageId(),
                userProfileImageResponse == null ? null : userProfileImageResponse.fileUrl()
        );
        return new UserProfileDetailResponse(myPageProfileDetailResponse, userInterviewSettingReadResponse);
    }

}
