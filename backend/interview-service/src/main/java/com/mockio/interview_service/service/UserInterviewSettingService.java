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
        UserInterviewSetting setting =
                UserInterviewSetting.createUserInterviewPreference(request.userId());

        userInterviewSettingRepository.insertIfAbsent(
                setting.getUserId(),
                setting.getTrack().name(),
                setting.getDifficulty().name(),
                setting.getFeedbackStyle().name(),
                setting.getInterviewMode().name(),
                setting.getAnswerTimeSeconds(),
                setting.getInterviewQuestionCount()
        );
    }

    /**
     * 면접 설정 조회
     * @param userId
     * @return
     */
    public UserInterviewSettingReadResponse getPreference(Long userId) {
        UserInterviewSetting byUserId = findByUserId(userId);
        return UserInterviewSettingMapper.from(byUserId);
    }

    /**
     * 면접 설정 수정
     *
     * @param userId
     * @param userRequest
     */
    public void updatePreference(Long userId, UserInterviewSettingUpdateRequest userRequest) {
        UserInterviewSetting byUserId = findByUserId(userId);
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
     * @param userId
     * @return
     */
    private UserInterviewSetting findByUserId(Long userId) {
       return userInterviewSettingRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomApiException(NOT_FOUND.value(), ERR_012, ERR_012.getMessage()));
    }

}
