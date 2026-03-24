package com.mockio.core_service.user.mapper;

import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.dto.response.MyPageProfileDetailResponse;
import com.mockio.core_service.user.dto.response.UserInterviewSettingReadResponse;
import com.mockio.core_service.user.dto.response.UserProfileDetailResponse;
import com.mockio.core_service.user.dto.response.UserProfileImageResponse;

/**
 * UserProfileMapper
 *
 * DTO / Domain 변환  Mapper.
 *
 */


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
