package com.mockio.interview_service.service;

/**
 * UserInterviewPreferenceService.
 *
 *  면접 설정 관련 서비스를 제공합니다.
 */

import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.interview_service.Mapper.InterviewMapper;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.dto.response.InterviewListResponse;
import com.mockio.interview_service.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewListResponse getInterviewList(String userId) {
        List<Interview> byinterview = interviewRepository.findByUserIdAndStatusAndEndedAtIsNullOrderByCreatedAt(userId, InterviewStatus.ACTIVE);
        return InterviewMapper.fromList(byinterview);
    }

}
