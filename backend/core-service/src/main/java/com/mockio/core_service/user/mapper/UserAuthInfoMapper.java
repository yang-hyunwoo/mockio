package com.mockio.core_service.user.mapper;

import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.dto.UserAuthInfoResponse;

public class UserAuthInfoMapper {

    public static UserAuthInfoResponse fromUserAuthInfo(User user) {
        return new UserAuthInfoResponse(
                user.getId(),
                user.getProfile().getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                user.getFailLoginCount(),
                user.getStatus().name()
        );
    }

    public static UserAuthInfoResponse fromOauthUserAuthInfo(User user , String name) {
        return new UserAuthInfoResponse(
                user.getId(),
                name,
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                user.getFailLoginCount(),
                user.getStatus().name()
        );
    }

}
