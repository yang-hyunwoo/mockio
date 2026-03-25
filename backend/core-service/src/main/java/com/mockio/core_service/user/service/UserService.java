package com.mockio.core_service.user.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.service.UserInterviewSettingService;
import com.mockio.core_service.user.constant.AuthProviderEnum;
import com.mockio.core_service.user.constant.UserStatus;
import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.domain.UserProfile;
import com.mockio.core_service.user.dto.UserAuthInfoResponse;
import com.mockio.core_service.user.dto.request.EnsureInterviewSettingRequest;
import com.mockio.core_service.user.dto.request.OauthUserRequest;
import com.mockio.core_service.user.dto.request.SignupRequest;
import com.mockio.core_service.user.dto.response.SignupResponse;
import com.mockio.core_service.user.dto.response.UserInfoResponse;
import com.mockio.core_service.internalmapper.InternalMapper;
import com.mockio.core_service.user.mapper.UserMapper;
import com.mockio.core_service.user.repository.UserProfileRepository;
import com.mockio.core_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.core_service.user.constant.error.UserErrorEnum.*;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final RecaptchaService recaptchaService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserInterviewSettingService userInterviewSettingService;

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
        userInterviewSettingService.ensureInterviewSettingSave(InternalMapper.toInternalEnsureInterviewSetting(new EnsureInterviewSettingRequest(save.getId())));
        return UserMapper.from(save);

    }

    public UserAuthInfoResponse getUserAuthInfo(String email) {
        User user = userRepository.findByEmailAndProvider(email, AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));

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

    public UserAuthInfoResponse oauthLogin(OauthUserRequest oauthUserRequest) {
        User user;
        if(userRepository.findByEmailAndProvider(oauthUserRequest.email(),
                oauthUserRequest.provider()).isPresent()) {
             user = userRepository.findByEmailAndProvider(oauthUserRequest.email(),
                    oauthUserRequest.provider()).orElseThrow(() -> new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        } else {
            user = User.createOautUser(oauthUserRequest);


            userRepository.save(user);

            UserProfile userProfile = UserProfile.createUserProfile(
                    user,
                    oauthUserRequest.name(),
                    oauthUserRequest.nickname()
            );
            userProfileRepository.save(userProfile);
            userInterviewSettingService.ensureInterviewSettingSave(InternalMapper.toInternalEnsureInterviewSetting(new EnsureInterviewSettingRequest(user.getId())));

        }
        String name = user.getProfile() != null && user.getProfile().getName() != null
                ? user.getProfile().getName()
                : oauthUserRequest.name();


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
