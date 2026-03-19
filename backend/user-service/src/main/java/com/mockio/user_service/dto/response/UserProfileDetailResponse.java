package com.mockio.user_service.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;

public record UserProfileDetailResponse(
        MyPageProfileDetailResponse userProfileResponse,
        UserInterviewSettingReadResponse interviewSettingResponse
) {}
