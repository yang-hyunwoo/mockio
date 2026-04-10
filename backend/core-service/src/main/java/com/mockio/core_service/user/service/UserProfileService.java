package com.mockio.core_service.user.service;

/**
 * UserService.
 *
 *  유저 관련 비즈니스 로직을 담당합니다.
 */

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.internalmapper.InternalMapper;
import com.mockio.core_service.interview.service.UserInterviewSettingService;
import com.mockio.core_service.user.client.FileServiceClient;
import com.mockio.core_service.user.constant.UserStatus;
import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.domain.UserProfile;
import com.mockio.core_service.user.dto.response.FileUploadResponse;
import com.mockio.core_service.user.dto.response.UserInterviewSettingReadResponse;
import com.mockio.core_service.user.dto.response.UserProfileDetailResponse;
import com.mockio.core_service.user.dto.response.UserProfileImageResponse;
import com.mockio.core_service.user.mapper.UserProfileMapper;
import com.mockio.core_service.user.repository.UserRepository;
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
    private final UserInterviewSettingService userInterviewSettingService;
    private final FileServiceClient fileServiceClient;

    /**
     * 유저 프로필 변경
     * @param userId
     * @param nickname
     * @param profileImage
     */
    public void updateMyProfile(Long userId, String nickname, MultipartFile profileImage) {

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
     * 유저 프로필 조회
     * @param
     * @return
     */
    public UserProfileDetailResponse getUserProfileDetail(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new CustomApiException(
                        NOT_FOUND.value(),
                        ERR_012,
                        ERR_012.getMessage()
                ));
        UserProfile userProfile = user.getProfile();

        UserInterviewSettingReadResponse preference = InternalMapper.fromInternalUserInterviewSettingRead(userInterviewSettingService.getPreference(userId));
        UserProfileImageResponse userProfileImageResponse = null;
        if(userProfile.getProfileImageId()!= null) {
            userProfileImageResponse = fileServiceClient.getProfileImage(userId, userProfile.getProfileImageId());
        }
        return UserProfileMapper.fromDetail(user,preference,userProfileImageResponse);
    }

    /**
     * 유저 정보 유무 조회
     * @param userId
     * @return
     */
    private UserProfile findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(
                        NOT_FOUND.value(),
                        ERR_012,
                        ERR_012.getMessage()
                ));
        return user.getProfile();
    }

}
