package com.mockio.interview_service.service;

/**
 * UserInterviewPreferenceService.
 *
 *  면접 설정 관련 서비스를 제공합니다.
 */

import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.common_jpa.dto.PageDto;
import com.mockio.interview_service.Mapper.InterviewMapper;
import com.mockio.interview_service.dto.response.InterviewPageResponse;
import com.mockio.interview_service.dto.response.InterviewMainListResponse;
import com.mockio.interview_service.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InterviewService {

    private final InterviewRepository interviewRepository;


    public InterviewMainListResponse getInterviewMainList(Long userId) {
        return InterviewMapper.fromMainList(interviewRepository.findByUserIdAndStatusAndEndedAtIsNullOrderByCreatedAt(userId, InterviewStatus.ACTIVE));
    }

    public PageDto<InterviewPageResponse> getInterviewList(Long userId, Pageable pageable) {
        return PageDto.of(interviewRepository.findByUserIdOrderByActiveFirst(userId,InterviewStatus.FAILED ,pageable),InterviewMapper::fromItem);
    }



}
