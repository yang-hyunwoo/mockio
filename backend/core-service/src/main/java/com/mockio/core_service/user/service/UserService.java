package com.mockio.core_service.user.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.service.UserInterviewSettingService;
import com.mockio.core_service.user.constant.AuthProviderEnum;
import com.mockio.core_service.user.constant.UserStatus;
import com.mockio.core_service.user.domain.PasswordResetToken;
import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.domain.UserProfile;
import com.mockio.core_service.user.dto.UserAuthInfoResponse;
import com.mockio.core_service.user.dto.request.*;
import com.mockio.core_service.user.dto.response.SignupResponse;
import com.mockio.core_service.user.dto.response.UserInfoResponse;
import com.mockio.core_service.internalmapper.InternalMapper;
import com.mockio.core_service.user.mapper.UserMapper;
import com.mockio.core_service.user.repository.UserProfileRepository;
import com.mockio.core_service.user.repository.UserRepository;
import com.mockio.core_service.util.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.mockio.core_service.user.constant.error.UserErrorEnum.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final RecaptchaService recaptchaService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserInterviewSettingService userInterviewSettingService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RedisService redisService;


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

    public void sendPasswordEmail(String email) {
        User user = userRepository.findByEmailAndProviderAndStatus(email, AuthProviderEnum.NORMAL, UserStatus.ACTIVE)
                .orElse(null);
        if(userNotNull(user)) {
            //redis 검증
            redisService.checkRateLimit(email);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                String token = UUID.randomUUID().toString();
                String link = "http://localhost:3000/password/reset?token=" + token;

                helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setTo(email);
                helper.setSubject("비밀번호 재설정");
                String html = loadHtmlTemplate().replace("{{link}}", link);

                String text = "비밀번호 재설정 링크:\n" + link;
                helper.setText(text, html);

                mailSender.send(mimeMessage);

                passwordResetTokenService.resetTokenGenerate(user.getId(),token);

            } catch (MessagingException e) {
                throw new CustomApiException(MAIL_SEND_FAIL.getHttpStatus(),
                        MAIL_SEND_FAIL,
                        MAIL_SEND_FAIL.getMessage());
            }
        }

    }

    public void resetPasswordChange(PasswordChangeRequest changeRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.validateToken(changeRequest.token());
        User user = userRepository.findByIdAndStatusAndProvider(passwordResetToken.getUserId(), UserStatus.ACTIVE, AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(USER_NOT_FOUND.getHttpStatus(), USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));

        user.resetPasswordChange(changeRequest.password(), changeRequest.confirmPassword(), passwordEncoder);
        passwordResetToken.updateResetToken();
    }

    public void deleteUser(User user, MypagePasswordChangeRequest request,HttpServletResponse response) {
        User findUser = userRepository.findByIdAndStatusAndProvider(
                user.getId(),
                UserStatus.ACTIVE,
                AuthProviderEnum.NORMAL
        ).orElseThrow(() -> new CustomApiException(
                USER_NOT_FOUND.getHttpStatus(),
                USER_NOT_FOUND,
                USER_NOT_FOUND.getMessage()
        ));

        passwordMatchCheck(request, findUser);
        findUser.withdraw();
        expireRefreshCookie(response);
    }

    public void updatePasswordChange(User user, MypagePasswordChangeRequest request) {
        User userRead = userRepository.findByIdAndStatusAndProvider(user.getId(),
                        UserStatus.ACTIVE,
                        AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(
                        USER_NOT_FOUND.getHttpStatus(),
                        USER_NOT_FOUND,
                        USER_NOT_FOUND.getMessage()
                ));

        passwordMatchCheck(request,userRead);
        user.resetPasswordChange(request.newPassword(), request.confirmPassword(), passwordEncoder);
    }

    private void expireRefreshCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // 로컬 http면 개발환경에 맞게 조정
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private static boolean userNotNull(User user) {
        return user != null;
    }

    private String loadHtmlTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("mail/password-reset.html");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomApiException(MAIL_LOAD_FAIL.getHttpStatus(),
                    MAIL_LOAD_FAIL,
                    MAIL_LOAD_FAIL.getMessage());
        }
    }

    /**
     * 패스워드 매칭 확인
     * @param request
     * @param findUser
     */
    private void passwordMatchCheck(MypagePasswordChangeRequest request, User findUser) {
        if (!passwordEncoder.matches(request.password(), findUser.getPassword())) {
            throw new CustomApiException(
                    PASSWORD_NOT_MATCH.getHttpStatus(),
                    PASSWORD_NOT_MATCH,
                    PASSWORD_NOT_MATCH.getMessage()
            );
        }
    }

}
