package com.mockio.user_service.mapper;

import com.mockio.user_service.domain.User;
import com.mockio.user_service.dto.request.SignupRequest;
import com.mockio.user_service.dto.response.SignupResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static User toEntity(SignupRequest sr, PasswordEncoder passwordEncoder) {
        return User.createUser(
                sr.email(),
                passwordEncoder.encode(sr.password())
                );
    }

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getProfile().getNickname(),
                user.getProvider()
        );
    }

}
