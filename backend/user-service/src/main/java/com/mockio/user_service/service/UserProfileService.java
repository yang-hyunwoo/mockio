package com.mockio.user_service.service;

/**
 * UserService.
 *
 *  유저 관련 비즈니스 로직을 담당합니다.
 */

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.user_service.client.FileServiceClient;
import com.mockio.user_service.domain.User;
import com.mockio.user_service.dto.response.*;
import com.mockio.user_service.mapper.UserProfileMapper;
import com.mockio.user_service.client.InterviewServiceClient;
import com.mockio.user_service.constant.UserStatus;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_012;
import static com.mockio.common_core.constant.CommonErrorEnum.ERR_500;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserProfileService {

    private final UserRepository userRepository;
    private final InterviewServiceClient interviewServiceClient;
    private final FileServiceClient fileServiceClient;

    /**
     * 유저 프로필 변경
     * @param userId
     * @param nickname
     * @param profileImage
     */
    public void updateMyProfile(Long userId , String nickname , MultipartFile profileImage) {
        Long profileImageId = null;
        UserProfile byId = findByUser(userId);

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                FileUploadResponse upload = fileServiceClient.upload(profileImage, byId.getProfileImageId(), userId);
                profileImageId = upload.fileGroupId();
            } catch (IOException e) {
                throw new CustomApiException(ERR_500.getHttpStatus(), ERR_500, ERR_500.getMessage());
            }
        }
        byId.applyPatch(nickname, profileImageId);

    }

    /**
     * 유저 탈퇴
     */
    public void deleteProfile(User user) {

    }

    /**
     * 유저 프로필 조회
     * @param
     * @return
     */
    public UserProfileDetailResponse getUserProfileDetail(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
        UserProfile userProfile = user.getProfile();

        UserInterviewSettingReadResponse userInterviewSettingReadResponse = interviewServiceClient.interviewSetting(userId);
        UserProfileImageResponse userProfileImageResponse = null;
        if(userProfile.getProfileImageId()!= null) {
            userProfileImageResponse = fileServiceClient.getProfileImage(userId, userProfile.getProfileImageId());
        }

        return UserProfileMapper.fromDetail(user,userInterviewSettingReadResponse,userProfileImageResponse);

    }

    /**
     * 유저 정보 유무 조회
     * @param userId
     * @return
     */
    private UserProfile findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
        return user.getProfile();
    }

}
