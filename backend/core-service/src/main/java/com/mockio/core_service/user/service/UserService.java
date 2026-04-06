package com.mockio.core_service.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_spring.util.CustomCookie;
import com.mockio.common_spring.util.EnvironmentProvider;
import com.mockio.core_service.interview.dto.request.InternalEnsureInterviewSettingRequest;
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
import com.mockio.core_service.user.kafka.domain.OutboxUserEvent;
import com.mockio.core_service.user.kafka.repository.OutboxUserEventRepository;
import com.mockio.core_service.user.mapper.UserAuthInfoMapper;
import com.mockio.core_service.user.mapper.UserMapper;
import com.mockio.core_service.user.repository.UserProfileRepository;
import com.mockio.core_service.user.repository.UserRepository;
import com.mockio.core_service.util.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final EnvironmentProvider environmentProvider;
    private final CustomCookie customCookie;
    private final OutboxUserEventRepository outboxUserEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * 회원 가입 로직
     * @param signupRequest
     * @return
     */
    public SignupResponse join(SignupRequest signupRequest) {

       // recaptchaService.verify(signupRequest.recaptchaToken());

        //1. 동일 유저 이메일 존재 검사 / 닉네임 중복 체크
        validateDuplicate(signupRequest);

        String password = passwordEncoder.encode(signupRequest.password());
        User saveUser = userRepository.save(UserMapper.toEntity(signupRequest, password));

        //유저 프로필 등록
        UserProfile userProfile = UserProfile.createUserProfile(
                saveUser,
                null,
                signupRequest.nickname()
        );

        userProfileRepository.save(userProfile);

        InternalEnsureInterviewSettingRequest internalEnsureInterviewSetting = InternalMapper.toInternalEnsureInterviewSetting(new EnsureInterviewSettingRequest(saveUser.getId()));
        JsonNode payloadJson = objectMapper.valueToTree(internalEnsureInterviewSetting);
        outboxUserEventRepository.save(
                OutboxUserEvent.createNew(
                        "User",
                        saveUser.getId(),
                        "signupAfterInterviewSetting",
                        payloadJson

                )
        );

//        userInterviewSettingService.ensureInterviewSettingSave(InternalMapper.toInternalEnsureInterviewSetting(new EnsureInterviewSettingRequest(saveUser.getId())));
        return UserMapper.fromSignUp(saveUser);

    }

    /**
     * user 로그인 시 회원 검증 (auth-service)
     * customUserDetailsService 참조
     * @param email
     * @return
     */
    public UserAuthInfoResponse getUserAuthInfo(String email) {
        User user = userRepository.findByEmailAndProvider(email, AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(
                        USER_NOT_FOUND.getHttpStatus(),
                        USER_NOT_FOUND,
                        USER_NOT_FOUND.getMessage()
                ));
        return UserAuthInfoMapper.fromUserAuthInfo(user);
    }

    /**
     * 사용자 정보 조회
     * @param userId
     * @return
     */
    public UserInfoResponse userDetail(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new CustomApiException(
                        USER_NOT_FOUND.getHttpStatus(),
                        USER_NOT_FOUND,
                        USER_NOT_FOUND.getMessage()
                ));
        return UserMapper.fromUserInfo(user);
    }

    /**
     * 로그인 성공 (실패 카운트 초기화 , 로그인 시간 저장)
     * @param userId
     */
    public void resetFailCount(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new CustomApiException(
                        USER_NOT_FOUND.getHttpStatus(),
                        USER_NOT_FOUND,
                        USER_NOT_FOUND.getMessage()
                ));
        user.updateLastLogin();
    }

    /**
     * 로그인 실패 (실패 카운트 증가)
     * @param email
     */
    public void loginFailure(String email) {
        User user = userRepository.findByEmailAndProvider(email, AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(
                        USER_NOT_FOUND.getHttpStatus(),
                        USER_NOT_FOUND,
                        USER_NOT_FOUND.getMessage()
                ));
        user.increaseFailLoginCount();
    }

    /**
     * oauth 로그인
     * @param oauthUserRequest
     * @return
     */
    public UserAuthInfoResponse oauthLogin(OauthUserRequest oauthUserRequest) {
        User user = userRepository.findByEmailAndProvider(
                oauthUserRequest.email(),
                oauthUserRequest.provider()
        ).orElseGet(() -> createOauthUser(oauthUserRequest));

        String name = extractOauthUserName(user, oauthUserRequest);

        return UserAuthInfoMapper.fromOauthUserAuthInfo(user, name);
    }

    /**
     * 비밀번호 변경 이메일 전송
     * @param email
     */
    public void sendPasswordEmail(String email) {
        User user = userRepository.findByEmailAndProviderAndStatus(
                email,
                AuthProviderEnum.NORMAL,
                UserStatus.ACTIVE
        ).orElse(null);

        if (user == null) {
            return;
        }

        redisService.checkRateLimit(email);

        String token = UUID.randomUUID().toString();
        String resetLink = createPasswordResetLink(token);

        passwordResetTokenService.resetTokenGenerate(user.getId(), token);

        try {
            MimeMessage mimeMessage = createPasswordResetMessage(email, resetLink);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomApiException(
                    MAIL_SEND_FAIL.getHttpStatus(),
                    MAIL_SEND_FAIL,
                    MAIL_SEND_FAIL.getMessage()
            );
        }
    }

    /**
     * 비밀번호 변경
     * @param changeRequest
     */
    public void resetPasswordChange(PasswordChangeRequest changeRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.validateToken(changeRequest.token());
        User user = userRepository.findByIdAndStatusAndProvider(passwordResetToken.getUserId(), UserStatus.ACTIVE, AuthProviderEnum.NORMAL)
                .orElseThrow(() -> new CustomApiException(
                        USER_NOT_FOUND.getHttpStatus(),
                        USER_NOT_FOUND,
                        USER_NOT_FOUND.getMessage()
                ));

        user.resetPasswordChange(changeRequest.password(), changeRequest.confirmPassword(), passwordEncoder);
        passwordResetToken.updateResetToken();
    }

    /**
     * 사용자 탈퇴
     * @param user
     * @param request
     * @param response
     */
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
        ResponseCookie refreshToken = customCookie.deleteCookie("refreshToken");
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
    }

    /**
     * (마이페이지) 비밀번호 변경
     * @param user
     * @param request
     */
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

    private String loadHtmlTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("mail/password-reset.html");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomApiException(
                    MAIL_LOAD_FAIL.getHttpStatus(),
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

    /**
     * 회원가입 동일 유저 이메일 존재 검사 / 닉네임 중복 체크
     * @param signupRequest
     */
    private void validateDuplicate(SignupRequest signupRequest) {
        userRepository.findByEmailAndProvider(signupRequest.email(), AuthProviderEnum.NORMAL).ifPresent(user -> {
            throw new CustomApiException(DUPLICATE_EMAIL.getHttpStatus(), DUPLICATE_EMAIL, DUPLICATE_EMAIL.getMessage());
        });
        if (userProfileRepository.existsByNickname(signupRequest.nickname())) {
            throw new CustomApiException(DUPLICATE_NICKNAME.getHttpStatus(), DUPLICATE_NICKNAME, DUPLICATE_NICKNAME.getMessage());
        }
    }

    /**
     * oauth 유저 등록
     * @param oauthUserRequest
     * @return
     */
    private User createOauthUser(OauthUserRequest oauthUserRequest) {
        User user = User.createOauthUser(oauthUserRequest);
        User savedUser = userRepository.save(user);

        UserProfile userProfile = UserProfile.createUserProfile(
                savedUser,
                oauthUserRequest.name(),
                oauthUserRequest.nickname()
        );
        userProfileRepository.save(userProfile);

        userInterviewSettingService.ensureInterviewSettingSave(
                InternalMapper.toInternalEnsureInterviewSetting(
                        new EnsureInterviewSettingRequest(savedUser.getId())
                )
        );

        return savedUser;
    }

    /**
     * ouath 유저 이름 조회
     * @param user
     * @param oauthUserRequest
     * @return
     */
    private String extractOauthUserName(User user, OauthUserRequest oauthUserRequest) {
        return user.getProfile() != null && user.getProfile().getName() != null
                ? user.getProfile().getName()
                : oauthUserRequest.name();
    }

    /**
     * 링크 생성
     * @param token
     * @return
     */
    private String createPasswordResetLink(String token) {
        String baseUrl = environmentProvider.isProd()
                ? "https://mockio.cloud"
                : "http://localhost:3000";

        return baseUrl + "/password/reset?token=" + token;
    }

    /**
     * 메일 메시지 생성
     * @param email
     * @param resetLink
     * @return
     * @throws MessagingException
     */
    private MimeMessage createPasswordResetMessage(String email, String resetLink) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("비밀번호 재설정");

        String html = loadHtmlTemplate().replace("{{link}}", resetLink);
        String text = "비밀번호 재설정 링크:\n" + resetLink;

        helper.setText(text, html);

        return mimeMessage;
    }

}
