package com.mockio.core_service.user.mapper;

import com.mockio.core_service.user.domain.PasswordResetToken;

public class PasswordResetTokenMapper {

    public static PasswordResetToken toEntity(Long userId,
                                              String token
    ) {
        return PasswordResetToken.createPwdResetToken(
                userId,
                token
        );
    }
}
