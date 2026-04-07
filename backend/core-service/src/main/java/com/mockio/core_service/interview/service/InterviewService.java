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

    /**
     * 면접 목록 메인 조회
     *
     * @param userId
     * @return
     */
    public InterviewMainListResponse getInterviewMainList(Long userId) {
        return InterviewMapper.fromMainList(interviewRepository.findByUserIdAndStatusAndEndedAtIsNullOrderByCreatedAt(userId, InterviewStatus.ACTIVE));
    }

    /**
     * 면접 페이징 리스트 조회 [실패 제외]
     *
     * @param userId
     * @param pageable
     * @return
     */
    public PageDto<InterviewPageResponse> getInterviewList(Long userId, Pageable pageable) {
        return PageDto.of(interviewRepository.findByUserIdOrderByActiveFirst(userId, InterviewStatus.FAILED, pageable), InterviewMapper::fromItem);
    }

    /**
     * 면접 종료
     *
     * @param userId
     * @param interviewId
     */
    public void interviewEnd(Long userId, Long interviewId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_FORBIDDEN.getHttpStatus(),
                        INTERVIEW_FORBIDDEN,
                        INTERVIEW_FORBIDDEN.getMessage())
                );
        interview.complete(InterviewEndReason.USER_EXIT);
    }

    /**
     * 사용자 면접 종료 (진행중인 면접 -> 종료)
     *
     * @param userId
     */
    public void activeInterviewEnd(Long userId) {
        Interview activeInterview = interviewRepository.findActiveByUserIdAndStatus(userId, ACTIVE)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage())
                );
        activeInterview.complete(InterviewEndReason.USER_EXIT);
    }

    /**
     * 면접 이력 페이징 조회
     *
     * @param userId
     * @param track
     * @param pageable
     * @return
     */
    public InterviewHistoryPageResponse getInterviewHistory(Long userId, InterviewTrack track, Pageable pageable) {
        Pageable scorePageable = PageRequest.of(0, 7, Sort.by(Sort.Direction.DESC, "endedAt"));

        List<Interview> scoreInterview = getScoreInterviewList(userId, track, scorePageable);
        ;
        Page<Interview> historyInterview = getInterviewHistoryPage(userId, track, pageable);
        List<Interview> historyContent = historyInterview.getContent();

        Collections.reverse(scoreInterview); // 차트용 asc

        if (scoreInterview.isEmpty()) {
            return createEmptyHistoryResponse(historyInterview);
        }

        Set<Long> interviewIds = Stream.concat(
                        scoreInterview.stream(),
                        historyContent.stream()
                )
                .map(Interview::getId)
                .collect(Collectors.toSet());

        //피드백 서비스 호출
        InternalInterviewScoreListResponse internalScoreResponse = feedbackService.getScoreHistory(new ArrayList<>(interviewIds));
        InterviewScoreListResponse interviewScoreList = InternalMapper.fromInterviewScoreList(internalScoreResponse);

        Map<Long, InterviewScoreListItem> scoreMap = createScoreMap(interviewScoreList);

        InterviewScoreHistoryResponse scoreHistory = InterviewMapper.fromScoreHistoryList(scoreInterview, scoreMap);

        InterviewHistoryResponse interviewHistoryResponse = InterviewMapper.fromHistoryList(historyContent, scoreMap);
        WeakPointResponse weakPointResponse = InterviewMapper.fromWeakPoint(interviewScoreList.scoreList());

        return InterviewMapper.fromHistoryResponse(scoreHistory, interviewHistoryResponse, historyInterview, weakPointResponse);
    }

    /**
     * 면접 점수 리스트 조회
     *
     * @param userId
     * @param track
     * @param scorePageable
     * @return
     */
    private List<Interview> getScoreInterviewList(Long userId, InterviewTrack track, Pageable scorePageable) {
        if (track == null) {
            return interviewRepository
                    .findByUserIdAndStatusAndEndReason(userId,
                            InterviewStatus.ENDED,
                            InterviewEndReason.COMPLETED,
                            scorePageable);
        } else {
            return interviewRepository
                    .findByUserIdAndStatusAndEndReasonAndTrack(
                            userId,
                            InterviewStatus.ENDED,
                            InterviewEndReason.COMPLETED,
                            track,
                            scorePageable
                    );
        }
    }

    /**
     * 면접 이력 메이지 조회
     *
     * @param userId
     * @param track
     * @param pageable
     * @return
     */
    private Page<Interview> getInterviewHistoryPage(Long userId, InterviewTrack track, Pageable pageable) {
        if (track == null) {
            return interviewRepository.findByUserIdAndStatusOrderByIdDesc(userId,
                    InterviewStatus.ENDED,
                    pageable
            );
        } else {
            return interviewRepository.findByUserIdAndStatusAndTrackOrderByIdDesc(
                    userId,
                    InterviewStatus.ENDED,
                    track,
                    pageable
            );
        }
    }

    private InterviewHistoryPageResponse createEmptyHistoryResponse(Page<Interview> historyInterview) {
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

    private Map<Long, InterviewScoreListItem> createScoreMap(InterviewScoreListResponse interviewScoreList) {
        return interviewScoreList.scoreList().stream()
                .collect(Collectors.toMap(
                        InterviewScoreListItem::interviewId,
                        item -> item
                ));
    }

}
