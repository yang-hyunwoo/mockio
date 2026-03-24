package com.mockio.core_service.user.mapper;

import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.dto.request.SignupRequest;
import com.mockio.core_service.user.dto.response.SignupResponse;
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
