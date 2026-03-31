package com.mockio.core_service.user.mapper;

import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.dto.request.SignupRequest;
import com.mockio.core_service.user.dto.response.SignupResponse;
import com.mockio.core_service.user.dto.response.UserInfoResponse;

public class UserMapper {

    public static User toEntity(SignupRequest sr , String password) {
        return User.createUser(
                sr.email(),
                password
                );
    }

    public static SignupResponse fromSignUp(User user) {
        return new SignupResponse(
                user.getId(),
                user.getProfile().getNickname(),
                user.getProvider()
        );
    }

    public static UserInfoResponse fromUserInfo(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getProfile().getNickname()
        );
    }

}
