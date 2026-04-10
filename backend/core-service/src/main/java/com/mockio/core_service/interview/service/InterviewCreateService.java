package com.mockio.core_service.interview.service;

import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.request.StartInterviewRequest;
import com.mockio.core_service.interview.repository.InterviewRepository;
import com.mockio.core_service.interview.repository.UserInterviewSettingRepository;
import com.mockio.core_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewStatus.ACTIVE;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.GENERATING;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.PENDING;

@Service
@RequiredArgsConstructor
public class InterviewCreateService {

    private final InterviewRepository interviewRepository;
    private final UserInterviewSettingRepository userInterviewSettingRepository;
    private final UserInterviewSettingService userInterviewSettingService;
    private final UserService userService;

    @Transactional
    public Long generateInterview(Long userId, StartInterviewRequest request) {

        // 같은 유저 인터뷰 생성 직렬화
        userService.findByIdForUpdate(userId);

        UserInterviewSetting userInterviewSetting = userInterviewSettingRepository.findByUserId(userId)
                .orElseGet(() -> userInterviewSettingService.absentEnsureSettingSave(userId));

        Long reusableInterviewId = resolveReusableInterview(userId);
        if (reusableInterviewId != null) {
            return reusableInterviewId;
        }

        return interviewRepository.findByUserIdAndIdempotencyKey(userId, request.idempotencyKey())
                .map(Interview::getId)
                .orElseGet(() -> saveNewInterview(userId, request, userInterviewSetting));
    }

    private Long saveNewInterview(Long userId,
                                  StartInterviewRequest request,
                                  UserInterviewSetting userInterviewSetting) {
        try {
            Interview interview = Interview.create(
                    request.idempotencyKey(),
                    userId,
                    userInterviewSetting.getTrack(),
                    userInterviewSetting.getDifficulty(),
                    userInterviewSetting.getFeedbackStyle(),
                    userInterviewSetting.getInterviewMode(),
                    userInterviewSetting.getAnswerTimeSeconds(),
                    userInterviewSetting.getInterviewQuestionCount()
            );
            Interview saveInterview = interviewRepository.save(interview);
            saveInterview.markAsRootInterview();
            return saveInterview.getId();
        } catch (DataIntegrityViolationException e) {
            return interviewRepository.findByUserIdAndIdempotencyKey(userId, request.idempotencyKey())
                    .orElseThrow(() -> e)
                    .getId();
        }
    }

    private Long resolveReusableInterview(Long userId) {
        return interviewRepository.findTopByUserIdAndStatusInOrderByCreatedAtDesc(
                        userId,
                        List.of(PENDING, GENERATING, ACTIVE)
                )
                .map(Interview::getId)
                .orElse(null);
    }
}