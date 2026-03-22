package com.mockio.user_service.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.user_service.constant.AuthProviderEnum;
import com.mockio.user_service.constant.error.UserErrorEnum;
import com.mockio.user_service.domain.User;
import com.mockio.user_service.dto.request.SignupRequest;
import com.mockio.user_service.dto.response.SignupResponse;
import com.mockio.user_service.mapper.UserMapper;
import com.mockio.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.user_service.constant.error.UserErrorEnum.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final RecaptchaService recaptchaService;
    private final UserRepository userRepository;

    public SignupResponse join(SignupRequest signupRequest) {
//        if (!recaptchaService.verify(signupRequest.recaptchaToken())) {
//            throw new CustomApiException(RECAPTCHA_ERROR.getHttpStatus(),
//                    RECAPTCHA_ERROR,
//                    RECAPTCHA_ERROR.getMessage());
//        }
        //1. 동일 유저 이메일 존재 검사
        userRepository.findByEmailAndProvider(signupRequest.email(), AuthProviderEnum.NORMAL).ifPresent(user -> {
            throw new CustomApiException(DUPLICATE_EMAIL.getHttpStatus(), DUPLICATE_EMAIL,DUPLICATE_EMAIL.getMessage());
        });
        if (userRepository.existsByNickname(signupRequest.nickname())) {
            throw new CustomApiException(DUPLICATE_NICKNAME.getHttpStatus(), DUPLICATE_NICKNAME, DUPLICATE_NICKNAME.getMessage());
        }
        User save = userRepository.save(UserMapper.toEntity(signupRequest,passwordEncoder));
        return UserMapper.from(save);

    }

}
