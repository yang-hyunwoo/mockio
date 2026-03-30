package com.mockio.core_service.interview.service;

/**
 * UserInterviewPreferenceService.
 *
 *  면접 설정 관련 서비스를 제공합니다.
 */

import com.mockio.common_ai_contractor.constant.InterviewEndReason;
import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_jpa.dto.PageDto;
import com.mockio.core_service.feedback.dto.response.InternalInterviewScoreListResponse;
import com.mockio.core_service.feedback.service.FeedbackService;
import com.mockio.core_service.internalmapper.InternalMapper;
import com.mockio.core_service.interview.Mapper.InterviewMapper;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.dto.response.*;
import com.mockio.core_service.interview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.ACTIVE;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final FeedbackService feedbackService;

    public InterviewMainListResponse getInterviewMainList(Long userId) {
        return InterviewMapper.fromMainList(interviewRepository.findByUserIdAndStatusAndEndedAtIsNullOrderByCreatedAt(userId, InterviewStatus.ACTIVE));
    }

    public PageDto<InterviewPageResponse> getInterviewList(Long userId, Pageable pageable) {
        return PageDto.of(interviewRepository.findByUserIdOrderByActiveFirst(userId,InterviewStatus.FAILED ,pageable),InterviewMapper::fromItem);
    }

    public void interviewEnd(Long userId , Long interviewId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(
                        () -> new CustomApiException(INTERVIEW_FORBIDDEN.getHttpStatus(), INTERVIEW_FORBIDDEN, INTERVIEW_FORBIDDEN.getMessage())
                );
        interview.complete(InterviewEndReason.USER_EXIT);
    }

    public void activeInterviewEnd(Long userId) {
        Interview activeInterview = interviewRepository.findActiveByUserIdAndStatus(userId, ACTIVE)
                        .orElseThrow(
                                () -> new CustomApiException(INTERVIEW_NOT_FOUND.getHttpStatus(),INTERVIEW_NOT_FOUND,INTERVIEW_NOT_FOUND.getMessage())
                        );
        activeInterview.complete(InterviewEndReason.USER_EXIT);
    }

    public InterviewHistoryPageResponse getInterviewHistory(Long userId, InterviewTrack track, Pageable pageable) {
        Pageable scorePageable = PageRequest.of(0, 7, Sort.by(Sort.Direction.DESC, "endedAt"));

        List<Interview> scoreInterview;
        Page<Interview> historyInterview;

        if (track == null) {
            scoreInterview = interviewRepository
                    .findByUserIdAndStatusAndEndReason(userId,
                            InterviewStatus.ENDED,
                            InterviewEndReason.COMPLETED,
                            scorePageable);

            historyInterview = interviewRepository.findByUserIdAndStatusOrderByIdDesc(userId,
                    InterviewStatus.ENDED,
                    pageable
            );

        } else {
            scoreInterview = interviewRepository
                    .findByUserIdAndStatusAndEndReasonAndTrack(
                            userId,
                            InterviewStatus.ENDED,
                            InterviewEndReason.COMPLETED,
                            track,
                            scorePageable
                    );

            historyInterview = interviewRepository.findByUserIdAndStatusAndTrackOrderByIdDesc(
                    userId,
                    InterviewStatus.ENDED,
                    track,
                    pageable
            );
        }
        Set<Long> interviewIds = Stream.concat(
                        scoreInterview.stream(),
                        historyInterview.getContent().stream()
                )
                .map(Interview::getId)
                .collect(Collectors.toSet());

        Collections.reverse(scoreInterview); // 차트용 asc

        if (scoreInterview.isEmpty()) {
            InterviewScoreHistoryResponse scoreHistory =
                    InterviewMapper.fromScoreHistoryList(List.of(), Map.of());

            InterviewHistoryResponse interviewHistoryResponse =
                    InterviewMapper.fromHistoryList(historyInterview.getContent(), Map.of());

            return InterviewMapper.fromHistoryResponse(
                    scoreHistory,
                    interviewHistoryResponse,
                    historyInterview,
                    null
            );
        }

        //피드백 서비스 호출
        InternalInterviewScoreListResponse scoreHistory1 = feedbackService.getScoreHistory(new ArrayList<>(interviewIds));
        InterviewScoreListResponse interviewScoreList = InternalMapper.fromInterviewScoreList(scoreHistory1);

        Map<Long, InterviewScoreListItem> scoreMap =
                interviewScoreList.scoreList().stream()
                        .collect(Collectors.toMap(
                                InterviewScoreListItem::interviewId,
                                item -> item
                        ));

        InterviewScoreHistoryResponse scoreHistory =InterviewMapper.fromScoreHistoryList(scoreInterview, scoreMap);

        InterviewHistoryResponse interviewHistoryResponse = InterviewMapper.fromHistoryList(historyInterview.getContent(), scoreMap);
        WeakPointResponse weakPointResponse = InterviewMapper.fromWeakPoint(interviewScoreList.scoreList());

        return InterviewMapper.fromHistoryResponse(scoreHistory,interviewHistoryResponse,historyInterview,weakPointResponse);
    }

}
