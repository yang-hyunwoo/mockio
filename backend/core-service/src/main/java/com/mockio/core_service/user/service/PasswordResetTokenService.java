package com.mockio.core_service.user.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.user.domain.PasswordResetToken;
import com.mockio.core_service.user.mapper.PasswordResetTokenMapper;
import com.mockio.core_service.user.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mockio.core_service.user.constant.error.UserErrorEnum.PASSWORD_TOKEN_NOT_VALID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public void resetTokenGenerate(Long userId, String token) {
        passwordResetTokenRepository.invalidateAll(userId);
        PasswordResetToken entity = PasswordResetTokenMapper.toEntity(userId, token);
        passwordResetTokenRepository.save(entity);
    }

    public PasswordResetToken validateToken(String token) {
        return passwordResetTokenRepository.findValidToken(token)
                .orElseThrow(
                        () -> new CustomApiException(PASSWORD_TOKEN_NOT_VALID.getHttpStatus(),
                                PASSWORD_TOKEN_NOT_VALID,
                                PASSWORD_TOKEN_NOT_VALID.getMessage())
                );
    }

}
