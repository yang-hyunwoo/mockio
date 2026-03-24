package com.mockio.user_service.mapper;

/**
 * UserProfileMapper
 *
 * DTO / Domain 변환  Mapper.
 *
 */

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.user_service.domain.User;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.response.*;

public class UserProfileMapper {



    public static UserProfileDetailResponse fromDetail(User user,
                                                       UserInterviewSettingReadResponse userInterviewSettingReadResponse,
                                                       UserProfileImageResponse userProfileImageResponse) {

        MyPageProfileDetailResponse myPageProfileDetailResponse = new MyPageProfileDetailResponse(
                user.getProfile().getNickname(),
                user.getEmail(),
                user.getProfile().getProfileImageId(),
                userProfileImageResponse == null ? null : userProfileImageResponse.fileUrl()
        );
        return new UserProfileDetailResponse(myPageProfileDetailResponse, userInterviewSettingReadResponse);
    }

}
