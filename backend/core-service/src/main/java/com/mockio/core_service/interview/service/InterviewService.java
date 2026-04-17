package com.mockio.core_service.interview.service;

/**
 * UserInterviewPreferenceService.
 *
 *  면접 설정 관련 서비스를 제공합니다.
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.InterviewEndReason;
import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareQuestionCommand;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummary;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummaryCommand;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_jpa.dto.PageDto;
import com.mockio.core_service.feedback.dto.response.InternalFeedbackDetailResponse;
import com.mockio.core_service.feedback.dto.response.InternalInterviewScoreListResponse;
import com.mockio.core_service.feedback.dto.response.InternalQuestionBoardDetailResponse;
import com.mockio.core_service.feedback.service.FeedbackService;
import com.mockio.core_service.internalmapper.InternalMapper;
import com.mockio.core_service.interview.Mapper.InterviewCompareMapper;
import com.mockio.core_service.interview.Mapper.InterviewMapper;
import com.mockio.core_service.interview.domain.*;
import com.mockio.core_service.interview.dto.request.InternalQuestionBoardCreateRequest;
import com.mockio.core_service.interview.dto.request.QuestionCompareRequest;
import com.mockio.core_service.interview.dto.response.*;
import com.mockio.core_service.interview.forward.ai.AIServiceClient;
import com.mockio.core_service.interview.kafka.domain.OutboxInterviewEvent;
import com.mockio.core_service.interview.kafka.repository.OutboxInterviewEventRepository;
import com.mockio.core_service.interview.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mockio.common_ai_contractor.constant.InterviewEndReason.COMPLETED;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.ACTIVE;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final FeedbackService feedbackService;
    private final InterviewFacadeService interviewFacadeService;
    private final AIServiceClient aiServiceClient;
    private final InterviewCompareSummaryRepository interviewCompareSummaryRepository;
    private final InterviewCompareQuestionRepository interviewCompareQuestionRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private static final String EVENT_TOPIC_COMPARE_QUESTION = "COMPAREQUESTION";
    private static final String EVENT_COMPARE_QUESTION_SUBMITTED = "compareQuestionSubmitted";
    private final ObjectMapper objectMapper;
    private final OutboxInterviewEventRepository outboxInterviewEventRepository;

    /**
     * 면접 목록 메인 조회
     *
     * @param userId
     * @return
     */
    public InterviewListResponse getInterviewMainList(Long userId) {
        return InterviewMapper.fromInterviewList(interviewRepository.findByUserIdAndStatusAndEndedAtIsNullOrderByCreatedAt(userId, InterviewStatus.ACTIVE));
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
     * 면접 비교 메서드
     *
     * @param userId
     * @param interviewId
     */
    public CompareInterviewResponse getCompareInterview(Long userId , Long interviewId) {
        //면접 유효성 검사
        Interview interview = getComparableInterview(userId, interviewId);
        Interview prevInterview = getPrevInterview(interview);

        //현재 피드백 조회
        InterviewResultResponse currentInterviewResult = interviewFacadeService.getInterviewHistoryDetail(interview.getId(), userId);

        //이전 피드백 조회
        InterviewResultResponse prevInterviewResult = interviewFacadeService.getInterviewHistoryDetail(prevInterview.getId(), userId);

        //현재 / 이전 질문-답변 조회
        List<CompareInterviewResponse.QuestionCompareItem> questionItems = getQuestionCompareItems(prevInterviewResult.questions(), currentInterviewResult.questions());

        return new CompareInterviewResponse(
                new CompareInterviewResponse.CompareItem(
                        interview.getId(),
                        currentInterviewResult.overallScore(),
                        currentInterviewResult.feedbackDimensions(),
                        currentInterviewResult.feedbackJobMetrics(),
                        currentInterviewResult.endedAt()
                ),
                new CompareInterviewResponse.CompareItem(
                        prevInterview.getId(),
                        prevInterviewResult.overallScore(),
                        prevInterviewResult.feedbackDimensions(),
                        prevInterviewResult.feedbackJobMetrics(),
                        prevInterviewResult.endedAt()
                ),
                questionItems
        );
    }

    /**
     * 면접 비교 요약
     * @param userId
     * @param interviewId
     * @return
     */
    public CompareInterviewSummaryResponse getCompareSummary(Long userId, Long interviewId) {
        Interview interview = getComparableInterview(userId, interviewId);
        Interview prevInterview = getPrevInterview(interview);

        InterviewCompareSummary interviewCompareSummary = interviewCompareSummaryRepository
                .findByCurrentInterviewIdAndPrevInterviewId(interview.getId(), prevInterview.getId())
                .orElseGet(() -> generateAndSaveCompareSummary(userId, interview, prevInterview));

        return InterviewCompareMapper.fromSummary(interviewCompareSummary);
    }

    /**
     * 이전 면접 질문 비교 ID로 조회
     * @param userId
     * @param compareQuestionId
     * @return
     */
    public CompareInterviewQuestionResponse getQuestionCompare(Long userId , Long compareQuestionId) {
        InterviewCompareQuestion interviewCompareQuestion = interviewCompareQuestionRepository.findById(compareQuestionId)
                .orElseThrow(
                        () -> new CustomApiException(
                                COMPARE_QUESTION_NOT_FOUND.getHttpStatus(),
                                COMPARE_QUESTION_NOT_FOUND,
                                COMPARE_QUESTION_NOT_FOUND.getMessage()
                        )
                );
        return InterviewCompareMapper.fromQuestion(interviewCompareQuestion);
    }

    /**
     * 이전 면접 질문 비교
     *
     * @param userId
     * @param req
     */
    public CompareInterviewQuestionResponse questionCompare(Long userId, QuestionCompareRequest req) {
        Interview interview = getComparableInterview(userId, req.interviewId());
        Interview prevInterview = getPrevInterview(interview);

        InterviewCompareQuestion existing =
                interviewCompareQuestionRepository
                        .findByInterviewIdAndCurrentQuestionIdAndPrevQuestionId(
                                req.interviewId(),
                                req.currentQuestionId(),
                                req.prevQuestionId()
                        )
                        .orElse(null);

        if(existing!=null) {
            return InterviewCompareMapper.fromQuestion(existing);
        }

        try {
            //면접 질문 조회
            InterviewQuestion interviewQuestion = interviewQuestionRepository.findByIdAndInterviewId(req.currentQuestionId(), interview.getId())
                    .orElseThrow(
                            () -> new CustomApiException(
                                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                                    INTERVIEW_FORBIDDEN,
                                    INTERVIEW_FORBIDDEN.getMessage()
                            )
                    );
            //현재 면접 답변 조회
            String currentAnswer = interviewAnswerRepository.findByQuestionId(req.currentQuestionId())
                    .map(InterviewAnswer::getAnswerText)
                    .orElseThrow(
                            () -> new CustomApiException(
                                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                                    INTERVIEW_FORBIDDEN,
                                    INTERVIEW_FORBIDDEN.getMessage()
                            ));

            //이전 면접 답변 조회
            String prevAnswer = interviewAnswerRepository.findByQuestionId(req.prevQuestionId())
                    .map(InterviewAnswer::getAnswerText)
                    .orElseThrow(
                            () -> new CustomApiException(
                                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                                    INTERVIEW_FORBIDDEN,
                                    INTERVIEW_FORBIDDEN.getMessage()
                            ));

            GeneratedCompareQuestionCommand generatedCompareQuestionCommand = new GeneratedCompareQuestionCommand(
                    interview.getTrack(),
                    interview.getFeedbackStyle(),
                    List.of("구조화",
                            "명확성",
                            "구체성",
                            "실무 적합성",
                            "의사결정 기준",
                            "트레이드오프 이해"),
                    interviewQuestion.getTitle(),
                    currentAnswer,
                    prevAnswer
            );

            //비교 면접 로우 pending
            InterviewCompareQuestion compareQuestionPending = InterviewCompareQuestion.createCompareQuestionPending(
                    req.interviewId(),
                    req.currentQuestionId(),
                    req.prevQuestionId()
            );

            InterviewCompareQuestion save = interviewCompareQuestionRepository.save(compareQuestionPending);
            publishEvent(EVENT_TOPIC_COMPARE_QUESTION, save.getId(), EVENT_COMPARE_QUESTION_SUBMITTED, generatedCompareQuestionCommand);

            return InterviewCompareMapper.fromQuestion(compareQuestionPending);

        } catch (DataIntegrityViolationException e) {
            InterviewCompareQuestion exist =
                    interviewCompareQuestionRepository
                            .findByInterviewIdAndCurrentQuestionIdAndPrevQuestionId(
                                    req.interviewId(),
                                    req.currentQuestionId(),
                                    req.prevQuestionId()
                            )
                            .orElseThrow(
                                    () -> new CustomApiException(
                                            COMPARE_QUESTION_NOT_FOUND.getHttpStatus(),
                                            COMPARE_QUESTION_NOT_FOUND,
                                            COMPARE_QUESTION_NOT_FOUND.getMessage())
                            );

            return InterviewCompareMapper.fromQuestion(exist);
        }
    }


    /**
     * 내부 호출 면접 목록 리스트 조회
     * @param userId
     */
    @Transactional(readOnly = true)
    public InterviewListResponse internalGetInterviewList(Long userId) {
        return InterviewMapper.fromInterviewList(interviewRepository.findByUserIdAndStatusAndEndReason(userId,InterviewStatus.ENDED, COMPLETED));

    }

    /**
     * 면접 정보 조회
     * @param userId
     * @param interviewId
     * @return
     */
    @Transactional(readOnly = true)
    public InternalQuestionAnswerResponse internalGetInterviewQuestionAnswer(Long userId, Long interviewId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(
                        () -> new CustomApiException(
                                INTERVIEW_FORBIDDEN.getHttpStatus(),
                                INTERVIEW_FORBIDDEN,
                                INTERVIEW_FORBIDDEN.getMessage()
                        )
                );

        List<InterviewQuestion> questions = interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interview.getId());
        List<Long> questionIds = questions.stream()
                .map(InterviewQuestion::getId)
                .toList();
        //면접 총 요약 조회
        FeedbackTotalDetailResponse interviewHistoryDetail = InternalMapper.fromInternalFeedbackTotalDetail(feedbackService.getFeedbackDetail(interviewId));

        List<InterviewAnswer> answers = interviewAnswerRepository.findAllByQuestionIdInOrderByIdAsc(questionIds);
        InterviewResultResponse interviewResultResponse = InterviewMapper.fromResult(interview,
                questions,
                answers,
                interviewHistoryDetail);
        return InterviewMapper.fromInternalQuestionAnswer(interviewResultResponse);
    }

    /**
     * 면접 정보 저장 시 정보 조회
     * @param req
     */
    public InternalQuestionBoardDetailResponse internalGetInterviewQuestionAnswerDetail(InternalQuestionBoardCreateRequest req) {
       if(req.interview().isEmpty()) {
           throw new CustomApiException(
                   INTERVIEW_FORBIDDEN.getHttpStatus(),
                   INTERVIEW_FORBIDDEN,
                   INTERVIEW_FORBIDDEN.getMessage()
           );
       }
        InternalQuestionBoardCreateRequest.Item interviewItem = req.interview().getFirst();

       //인터뷰 조회
        Interview interview = interviewRepository.findByIdAndUserId(interviewItem.interviewId(), req.userId())
                .orElseThrow(
                        () -> new CustomApiException(
                                INTERVIEW_FORBIDDEN.getHttpStatus(),
                                INTERVIEW_FORBIDDEN,
                                INTERVIEW_FORBIDDEN.getMessage()
                        )
                );

        //인터뷰 질문 조회
        InterviewQuestion interviewQuestion = interviewQuestionRepository.findByIdAndInterviewId(interviewItem.questionId(), interviewItem.interviewId())
                .orElseThrow(
                        () -> new CustomApiException(
                                INTERVIEW_FORBIDDEN.getHttpStatus(),
                                INTERVIEW_FORBIDDEN,
                                INTERVIEW_FORBIDDEN.getMessage()
                        )
                );

        //인터뷰 답변 조회
        InterviewAnswer interviewAnswer = interviewAnswerRepository.findByQuestionId(interviewItem.questionId())
                .orElseThrow(
                        () -> new CustomApiException(
                                INTERVIEW_FORBIDDEN.getHttpStatus(),
                                INTERVIEW_FORBIDDEN,
                                INTERVIEW_FORBIDDEN.getMessage()
                        )
                );

        //ai 요약
        InternalFeedbackDetailResponse internalFeedbackDetailResponse = feedbackService.interviewFeedbackRead(interviewAnswer.getId());

        return new InternalQuestionBoardDetailResponse(
                interview.getTrack().name(),
                interview.getId(),
                interviewQuestion.getId(),
                interviewQuestion.getSeq(),
                interviewQuestion.getQuestionText(),
                interviewAnswer.getId(),
                interviewAnswer.getAnswerText(),
                internalFeedbackDetailResponse.score(),
                internalFeedbackDetailResponse.summary()
        );
    }


    /**
     * 이전 면접 ai 호출
     * @param userId
     * @param interview
     * @param prevInterview
     * @return
     */
    private InterviewCompareSummary generateAndSaveCompareSummary(
            Long userId,
            Interview interview,
            Interview prevInterview
    ) {
        InterviewResultResponse currentInterviewResult =
                interviewFacadeService.getInterviewHistoryDetail(interview.getId(), userId);

        InterviewResultResponse prevInterviewResult =
                interviewFacadeService.getInterviewHistoryDetail(prevInterview.getId(), userId);

        //이전 면접 기본 질문 만 조회
        List<InterviewResultResponse.QuestionItem> preList = prevInterviewResult.questions()
                .stream()
                .filter(q -> q.questionOrder() % 10 == 0)
                .toList();

        //현재 면접 기본 질문 만 조회
        List<InterviewResultResponse.QuestionItem> currentList = currentInterviewResult.questions()
                .stream()
                .filter(q -> q.questionOrder() % 10 == 0)
                .toList();

        List<CompareInterviewResponse.QuestionCompareItem> questionItems =
                getQuestionCompareItems(preList, currentList);

        List<GeneratedCompareSummaryCommand.Item> items =
                selectRepresentativeQuestions(questionItems);

        GeneratedCompareSummaryCommand command =
                GeneratedCompareSummaryCommandAssemble(
                        interview.getTrack(),
                        interview.getFeedbackStyle(),
                        prevInterviewResult,
                        currentInterviewResult,
                        items,
                        questionItems
                );

        GeneratedCompareSummary aiResult =
                aiServiceClient.generateCompareInterview(command);

        InterviewCompareSummary entity =
                InterviewCompareSummary.createInterviewCompareSummary(
                        interview.getId(),
                        prevInterview.getId(),
                        command.totalCount(),
                        command.betterCount(),
                        command.notCount(),
                        aiResult.headline(),
                        aiResult.summary(),
                        aiResult.strengths(),
                        aiResult.improvements(),
                        aiResult.improvementTags(),
                        aiResult.provider(),
                        aiResult.model(),
                        aiResult.promptVersion(),
                        aiResult.temperature()
                );

        try {
            return interviewCompareSummaryRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            return interviewCompareSummaryRepository
                    .findByCurrentInterviewIdAndPrevInterviewId(interview.getId(), prevInterview.getId())
                    .orElseThrow(() -> e);
        }
    }

    /**
     * 요약 request 생성
     * @param track
     * @param prevInterviewResult
     * @param currentInterviewResult
     * @param items
     * @param questionItems
     * @return
     */
    private GeneratedCompareSummaryCommand GeneratedCompareSummaryCommandAssemble(
            InterviewTrack track,
            InterviewFeedbackStyle feedbackStyle,
            InterviewResultResponse prevInterviewResult,
            InterviewResultResponse currentInterviewResult,
            List<GeneratedCompareSummaryCommand.Item> items,
            List<CompareInterviewResponse.QuestionCompareItem> questionItems
    ) {
        Integer totalCount = (int) questionItems.stream()
                .filter(q -> q.seq() != null && q.seq() % 10 == 0)
                .filter(q -> q.previousScore() != null && q.currentScore() != null)
                .filter(q -> q.previousAnswer() != null && q.currentAnswer() != null)
                .count();
        Integer betterCount = (int) questionItems.stream()
                .filter(q -> q.seq() != null && q.seq() % 10 == 0)
                .filter(q -> q.currentScore() > q.previousScore())
                .count();
        Integer notCount = (int) questionItems.stream()
                .filter(q -> q.seq() != null && q.seq() % 10 == 0)
                .filter(q -> q.previousScore() > q.currentScore())
                .count();
        return new GeneratedCompareSummaryCommand(
                track,
                feedbackStyle,
                new GeneratedCompareSummaryCommand.Interview(
                        prevInterviewResult.overallScore(),
                        prevInterviewResult.feedbackDimensions(),
                        prevInterviewResult.feedbackJobMetrics(),
                        prevInterviewResult.summary()
                ),
                new GeneratedCompareSummaryCommand.Interview(
                        currentInterviewResult.overallScore(),
                        currentInterviewResult.feedbackDimensions(),
                        currentInterviewResult.feedbackJobMetrics(),
                        currentInterviewResult.summary()
                ),
                totalCount,
                betterCount,
                notCount,
                items
        );
    }

    /**
     * 현재 / 이전 질문-답변 조회
     * @param prevQuestionList
     * @param currentQuestionList
     * @return
     */
    private static List<CompareInterviewResponse.QuestionCompareItem> getQuestionCompareItems(List<InterviewResultResponse.QuestionItem> prevQuestionList,
                                                                                              List<InterviewResultResponse.QuestionItem> currentQuestionList
    ) {
        Map<Integer, InterviewResultResponse.QuestionItem> prevMap = prevQuestionList.stream()
                .collect(Collectors.toMap(
                        InterviewResultResponse.QuestionItem::questionOrder,
                        Function.identity(),
                        (a, b) -> a
                ));

        return currentQuestionList.stream()
                .map(current -> {
                    InterviewResultResponse.QuestionItem prev = prevMap.get(current.questionOrder());

                    return new CompareInterviewResponse.QuestionCompareItem(
                            current != null ? current.id() : null,
                            prev != null ? prev.id() : null,
                            current.question(),
                            current.questionOrder(),
                            prev != null ? prev.answer() : null,
                            current.answer(),
                            prev != null ? prev.score() : null,
                            current.score()
                    );
                })
                .toList();
    }

    /**
     * 이전 비교 인터뷰 조회
     * @param interview
     * @return
     */
    private Interview getPrevInterview(Interview interview) {
        return interviewRepository
              .findTopByRootInterviewIdAndCreatedAtBeforeAndEndReasonAndStatusOrderByCreatedAtDesc(
                      interview.getRootInterview().getId(),
                      interview.getCreatedAt(),
                      COMPLETED,
                      InterviewStatus.ENDED
              )
              .orElseThrow(
                      () -> new CustomApiException(
                              INTERVIEW_FORBIDDEN.getHttpStatus(),
                              INTERVIEW_FORBIDDEN,
                              INTERVIEW_FORBIDDEN.getMessage()
                      ));
    }

    /**
     * 면접 유효성 검사
     * @param userId
     * @param interviewId
     */
    private Interview getComparableInterview(Long userId, Long interviewId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId,
                userId
        ).orElseThrow(
                () -> new CustomApiException(
                        INTERVIEW_FORBIDDEN.getHttpStatus(),
                        INTERVIEW_FORBIDDEN,
                        INTERVIEW_FORBIDDEN.getMessage()
                )
        );
        if (interview.getStatus() != InterviewStatus.ENDED ||
                interview.getEndReason() != COMPLETED) {

            throw new CustomApiException(
                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                    INTERVIEW_FORBIDDEN,
                    INTERVIEW_FORBIDDEN.getMessage()
            );
        }
        return interview;
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
                            COMPLETED,
                            scorePageable);
        } else {
            return interviewRepository
                    .findByUserIdAndStatusAndEndReasonAndTrack(
                            userId,
                            InterviewStatus.ENDED,
                            COMPLETED,
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

    /**
     * 빈 면접 생성
     * @param historyInterview
     * @return
     */
    private InterviewHistoryPageResponse createEmptyHistoryResponse(Page<Interview> historyInterview) {
        return InterviewMapper.fromHistoryResponse(
                InterviewMapper.fromScoreHistoryList(List.of(), Map.of()),
                InterviewMapper.fromHistoryList(historyInterview.getContent(), Map.of()),
                historyInterview,
                null
        );
    }

    /**
     * 면접 점수 map 생성
     * @param interviewScoreList
     * @return
     */
    private Map<Long, InterviewScoreListItem> createScoreMap(InterviewScoreListResponse interviewScoreList) {
        return interviewScoreList.scoreList().stream()
                .collect(Collectors.toMap(
                        InterviewScoreListItem::interviewId,
                        item -> item
                ));
    }

    /**
     * 면접 질문 3개 요약
     * @param questions
     * @return
     */
    private List<GeneratedCompareSummaryCommand.Item> selectRepresentativeQuestions(
            List<CompareInterviewResponse.QuestionCompareItem> questions
    ) {
        List<CompareInterviewResponse.QuestionCompareItem> basicQuestions = questions.stream()
                .filter(q -> q.seq() != null && q.seq() % 10 == 0)
                .filter(q -> q.previousScore() != null && q.currentScore() != null)
                .filter(q -> q.previousAnswer() != null && q.currentAnswer() != null)
                .toList();

        if (basicQuestions.isEmpty()) {
            return List.of();
        }

        List<GeneratedCompareSummaryCommand.Item> result = new ArrayList<>();
        Set<Long> selectedIds = new HashSet<>();

        // 1. 가장 많이 좋아진 질문
        basicQuestions.stream()
                .max(Comparator.comparingInt(q -> q.currentScore() - q.previousScore()))
                .ifPresent(q -> {
                    result.add(toRepresentative("MOST_IMPROVED", q));
                    selectedIds.add(q.currentQuestionId());
                });

        // 2. 현재 가장 약한 질문
        basicQuestions.stream()
                .filter(q -> !selectedIds.contains(q.currentQuestionId()))
                .min(Comparator.comparingInt(CompareInterviewResponse.QuestionCompareItem::currentScore))
                .ifPresent(q -> {
                    result.add(toRepresentative("WEAKEST_CURRENT", q));
                    selectedIds.add(q.currentQuestionId());
                });

        // 3. 평균적인 질문
        double avgCurrentScore = basicQuestions.stream()
                .mapToInt(CompareInterviewResponse.QuestionCompareItem::currentScore)
                .average()
                .orElse(0);

        basicQuestions.stream()
                .filter(q -> !selectedIds.contains(q.currentQuestionId()))
                .min(Comparator.comparingDouble(q -> Math.abs(q.currentScore() - avgCurrentScore)))
                .ifPresent(q -> {
                    result.add(toRepresentative("REPRESENTATIVE_BASIC", q));
                    selectedIds.add(q.currentQuestionId());
                });

        return result;
    }

    private GeneratedCompareSummaryCommand.Item toRepresentative(
            String type,
            CompareInterviewResponse.QuestionCompareItem q
    ) {
        return new GeneratedCompareSummaryCommand.Item(
                type,
                q.currentQuestionId(),
                q.prevQuestionId(),
                q.title(),
                q.previousAnswer(),
                q.currentAnswer(),
                q.previousScore(),
                q.currentScore(),
                q.currentScore() - q.previousScore()
        );
    }

    /**
     * outbox 테이블 저장
     */
    private void publishEvent(String topic, Long aggregateId, String eventType, Object payload) {
        JsonNode payloadJson = objectMapper.valueToTree(payload);

        outboxInterviewEventRepository.save(
                OutboxInterviewEvent.createNew(
                        topic,
                        aggregateId,
                        eventType,
                        payloadJson
                )
        );
    }

}
