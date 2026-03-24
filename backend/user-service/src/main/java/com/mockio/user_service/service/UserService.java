package com.mockio.user_service.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.user_service.client.InterviewServiceClient;
import com.mockio.user_service.constant.AuthProviderEnum;
import com.mockio.user_service.constant.UserStatus;
import com.mockio.user_service.constant.error.UserErrorEnum;
import com.mockio.user_service.domain.User;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.UserAuthInfoResponse;
import com.mockio.user_service.dto.request.SignupRequest;
import com.mockio.user_service.dto.response.SignupResponse;
import com.mockio.user_service.dto.response.UserInfoResponse;
import com.mockio.user_service.mapper.UserMapper;
import com.mockio.user_service.repository.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;
    private final InterviewServiceClient interviewServiceClient;

    public SignupResponse join(SignupRequest signupRequest) {
//        if (!recaptchaService.verify(signupRequest.recaptchaToken())) {
//            throw new CustomApiException(RECAPTCHA_ERROR.getHttpStatus(),
//                    RECAPTCHA_ERROR,
//                    RECAPTCHA_ERROR.getMessage());
//        }
        //1. 동일 유저 이메일 존재 검사
        userRepository.findByEmailAndProvider(signupRequest.email(), AuthProviderEnum.NORMAL).ifPresent(user -> {
            throw new CustomApiException(DUPLICATE_EMAIL.getHttpStatus(), DUPLICATE_EMAIL, DUPLICATE_EMAIL.getMessage());
        });
        if (userProfileRepository.existsByNickname(signupRequest.nickname())) {
            throw new CustomApiException(DUPLICATE_NICKNAME.getHttpStatus(), DUPLICATE_NICKNAME, DUPLICATE_NICKNAME.getMessage());
        }
        User save = userRepository.save(UserMapper.toEntity(signupRequest, passwordEncoder));

        //유저 프로필 등록
        UserProfile userProfile = UserProfile.createUserProfile(
                save,
                null,
                signupRequest.nickname()
        );
        userProfileRepository.save(userProfile);
        interviewServiceClient.ensureInterviewSetting(save.getId());

        return UserMapper.from(save);

    }

    public UserAuthInfoResponse getUserAuthInfo(String email) {
        User user = userRepository.findByEmailAndProvider(email, AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));

        return new UserAuthInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                user.getFailLoginCount(),
                user.getStatus().name()
        );
    }

    public UserInfoResponse userDetail(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));

        return new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getProfile().getNickname()
        );
    }

    public void resetFailCount(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        user.updateLastLogin();
        user.resetFailLoginCount();
    }

    public void loginFailure(String email) {
        User user = userRepository.findByEmailAndProvider(email, AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        user.increaseFailLoginCount();
    }

}
