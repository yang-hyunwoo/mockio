package com.mockio.interview_service.service;

/**
 * UserInterviewPreferenceService.
 *
 *  면접 설정 관련 서비스를 제공합니다.
 */

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.interview_service.Mapper.UserInterviewSettingMapper;
import com.mockio.interview_service.domain.UserInterviewSetting;
import com.mockio.interview_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.interview_service.dto.request.UserInterviewSettingUpdateRequest;
import com.mockio.interview_service.dto.response.UserInterviewSettingReadResponse;
import com.mockio.interview_service.repository.UserInterviewSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_012;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserInterviewSettingService {

    private final UserInterviewSettingRepository userInterviewSettingRepository;

    /**
     * 초기 면접 설정 저장
     */
    public void ensureInterviewSettingSave(EnsureInterviewSettingRequest request) {
        try {
            userInterviewSettingRepository.save(UserInterviewSetting.createUserInterviewPreference(request.keycloakId()));
        } catch (DataIntegrityViolationException e) {
            // 이미 존재 → ensure 관점에서는 정상
            // no-op
        }
    }

    /**
     * 면접 설정 조회
     * @param keycloakId
     * @return
     */
    public UserInterviewSettingReadResponse getPreference(String keycloakId) {
        UserInterviewSetting byUserId = findByUserId(keycloakId);
        return UserInterviewSettingMapper.from(byUserId);
    }

    /**
     * 면접 설정 수정
     *
     * @param keycloakId
     * @param userRequest
     */
    public void updatePreference(String keycloakId, UserInterviewSettingUpdateRequest userRequest) {
        UserInterviewSetting byUserId = findByUserId(keycloakId);
        byUserId.applyPatch(userRequest.track(),
                userRequest.difficulty(),
                userRequest.feedbackStyle(),
                userRequest.interviewMode(),
                userRequest.answerTimeSeconds(),
                userRequest.interviewQuestionCount()
        );
    }

    /**
     * 면접 질문 테이블 조회
     * @param keycloakId
     * @return
     */
    private UserInterviewSetting findByUserId(String keycloakId) {
       return userInterviewSettingRepository.findByUserId(keycloakId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
    }

}
