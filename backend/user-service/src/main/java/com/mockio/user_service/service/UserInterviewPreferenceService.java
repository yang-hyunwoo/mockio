package com.mockio.user_service.service;

/**
 * UserInterviewPreferenceService.
 *
 *  면접 설정 관련 서비스를 제공합니다.
 */

import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.user_service.Mapper.UserInterviewPreferenceMapper;
import com.mockio.user_service.domain.UserInterviewPreference;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.request.UserInterviewPreferenceUpdateRequest;
import com.mockio.user_service.dto.response.UserInterviewPreferenceReadResponse;
import com.mockio.user_service.repository.UserInterviewPreferenceRepository;
import com.mockio.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.common_spring.constant.CommonErrorEnum.ERR_012;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserInterviewPreferenceService {

    private final UserInterviewPreferenceRepository userInterviewPreferenceRepository;
    private final UserProfileRepository userRepository;

    /**
     * 면접 설정 조회
     * @param keycloakId
     * @return
     */
    public UserInterviewPreferenceReadResponse getPreference(String keycloakId) {
        UserProfile byKeycloakId = findByKeycloakId(keycloakId);
        UserInterviewPreference userInterviewPreference = userInterviewPreferenceRepository.findById(byKeycloakId.getId())
                .orElseThrow(
                        () -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage())
                );
        return UserInterviewPreferenceMapper.from(userInterviewPreference);

    }

    /**
     * 면접 설정 수정
     * @param keycloakId
     * @param userInterviewPreferenceUpdateRequest
     */
    public void updatePreference(String keycloakId,
                                 UserInterviewPreferenceUpdateRequest userInterviewPreferenceUpdateRequest) {
        UserProfile byKeycloakId = findByKeycloakId(keycloakId);
        UserInterviewPreference userInterviewPreference = userInterviewPreferenceRepository.findById(byKeycloakId.getId())
                .orElseThrow(
                        () -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage())
                );
        userInterviewPreference.update(userInterviewPreferenceUpdateRequest.track(),
                userInterviewPreferenceUpdateRequest.difficulty(),
                userInterviewPreferenceUpdateRequest.feedbackStyle(),
                userInterviewPreferenceUpdateRequest.interviewMode(),
                userInterviewPreferenceUpdateRequest.answerTimeSeconds());

    }



    /**
     * 유저 정보 유무 조회
     * @param keycloakId
     * @return
     */
    private UserProfile findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
    }
}
