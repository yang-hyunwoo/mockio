package com.mockio.core_service.user.dto.response;

public record UserProfileDetailResponse(
        MyPageProfileDetailResponse userProfileResponse,
        UserInterviewSettingReadResponse interviewSettingResponse
) {}
