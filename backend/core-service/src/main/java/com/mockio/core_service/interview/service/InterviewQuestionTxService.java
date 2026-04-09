package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.Mapper.InterviewQuestionMapper;
import com.mockio.core_service.interview.constant.QuestionType;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.request.InterviewGenerateContext;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.dto.response.SelectedQuestion;
import com.mockio.core_service.interview.dto.response.SelectionPolicy;
import com.mockio.core_service.interview.repository.InterviewQuestionRepository;
import com.mockio.core_service.interview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_ALREADY_ACTIVE;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_FORBIDDEN;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_NOT_FOUND;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INVALID_INTERVIEW_STATUS;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.ACTIVE;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.GENERATING;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.PENDING;

@Service
@RequiredArgsConstructor
public class InterviewQuestionTxService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final UserInterviewSettingService userInterviewSettingService;

    @Transactional
    public InterviewGenerateContext prepareGenerate(Long interviewId, Long userId) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (interview.getStatus() != PENDING
                && interview.getStatus() != GENERATING
                && interview.getStatus() != ACTIVE) {
            throw new CustomApiException(
                    INVALID_INTERVIEW_STATUS.getHttpStatus(),
                    INVALID_INTERVIEW_STATUS,
                    INVALID_INTERVIEW_STATUS.getMessage()
            );
        }

        if (interview.isQuestionGenerated()) {
            return InterviewGenerateContext.generated();
        }

        if (interview.getStatus() == PENDING) {
            interview.markGenerating();
        }

        return InterviewGenerateContext.of(
                interview.getTrack(),
                interview.getDifficulty(),
                interview.getInterviewMode(),
                interview.getAnswerTimeSeconds(),
                interview.getCount()
        );
    }

    @Transactional
    public void completeGenerate(Long interviewId, Long userId, GeneratedQuestion generatedQuestion) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (interview.isQuestionGenerated()) {
            return;
        }

        //이전 면접 질문 조회 (primaryTag, title, text)
        List<InterviewQuestion> historyInterview = getInterviewQuestions(userId);


        List<SelectedQuestion> selectedQuestions = selectQuestions(generatedQuestion.questions(), historyInterview, interview.getCount());

        List<InterviewQuestion> entities = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();
        for (int i = 0; i < selectedQuestions.size(); i++) {
            SelectedQuestion q = selectedQuestions.get(i);
            int order = (i + 1) * 10;
            entities.add(InterviewQuestion.createInterviewQuestion(
                    interview,
                    order,
                    q.title(),
                    q.primaryTag(),
                    q.tags(),
                    q.body(),
                    q.provider(),
                    q.model(),
                    q.promptVersion(),
                    q.temperature(),
                    now
            ));
        }

        interviewQuestionRepository.saveAll(entities);
        interview.markGenerated();
    }

    @Transactional
    public void failGenerate(Long interviewId, Long userId, String errorMessage) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        interview.markGenerateFailed(errorMessage);
    }

    @Transactional(readOnly = true)
    public InterviewQuestionReadResponse getQuestions(Long interviewId, Long userId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (!interview.getUserId().equals(userId)) {
            throw new CustomApiException(
                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                    INTERVIEW_FORBIDDEN,
                    INTERVIEW_FORBIDDEN.getMessage()
            );
        }

        return InterviewQuestionMapper.fromList(
                interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interviewId),
                false,
                interview
        );
    }

    @Transactional
    public InterviewQuestionReadResponse retryInterview(Long userId, RetryInterviewRequest request) {
        validateNoActiveInterview(userId);

        Interview sourceInterview = getRetrySourceInterview(userId, request.interviewId());
        Interview savedRetryInterview = createRetryInterview(userId, request.idempotencyKey(), sourceInterview);
        Interview lockedRetryInterview = getRetryInterviewForUpdate(userId, savedRetryInterview.getId());

        validateRetryInterviewStatus(lockedRetryInterview);

        if (lockedRetryInterview.isQuestionGenerated()) {
            return getQuestions(lockedRetryInterview.getId(), userId);
        }

        lockedRetryInterview.markGenerating();

        List<InterviewQuestion> baseQuestions = getBaseQuestions(sourceInterview.getId());
        List<InterviewQuestion> copiedQuestions = copyBaseQuestions(lockedRetryInterview, baseQuestions);

        interviewQuestionRepository.saveAll(copiedQuestions);
        lockedRetryInterview.markGenerated();

        return getQuestions(lockedRetryInterview.getId(), userId);
    }

    private void validateNoActiveInterview(Long userId) {
        if (interviewRepository.existsByUserIdAndStatus(userId, ACTIVE)) {
            throw new CustomApiException(
                    INTERVIEW_ALREADY_ACTIVE.getHttpStatus(),
                    INTERVIEW_ALREADY_ACTIVE,
                    INTERVIEW_ALREADY_ACTIVE.getMessage()
            );
        }
    }

    private Interview getRetrySourceInterview(Long userId, Long interviewId) {
        return interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_FORBIDDEN.getHttpStatus(),
                        INTERVIEW_FORBIDDEN,
                        INTERVIEW_FORBIDDEN.getMessage()
                ));
    }

    private Interview createRetryInterview(Long userId, String idempotencyKey, Interview sourceInterview) {
        return interviewRepository.save(
                Interview.reInterviewCreate(idempotencyKey, userId, sourceInterview)
        );
    }

    private Interview getRetryInterviewForUpdate(Long userId, Long interviewId) {
        return interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));
    }

    private void validateRetryInterviewStatus(Interview interview) {
        if (interview.getStatus() != ACTIVE) {
            throw new CustomApiException(
                    INVALID_INTERVIEW_STATUS.getHttpStatus(),
                    INVALID_INTERVIEW_STATUS,
                    INVALID_INTERVIEW_STATUS.getMessage()
            );
        }
    }

    private List<InterviewQuestion> getBaseQuestions(Long interviewId) {
        return interviewQuestionRepository.findByInterviewIdAndTypeOrderBySeqAsc(interviewId, QuestionType.BASE);
    }

    private List<InterviewQuestion> copyBaseQuestions(Interview retryInterview, List<InterviewQuestion> sourceQuestions) {
        return sourceQuestions.stream()
                .map(source -> InterviewQuestion.createInterviewQuestion(
                        retryInterview,
                        source.getSeq(),
                        source.getTitle(),
                        source.getPrimaryTag(),
                        source.getTags() == null ? new LinkedHashSet<>() : new LinkedHashSet<>(source.getTags()),
                        source.getQuestionText(),
                        source.getProvider(),
                        source.getModel(),
                        source.getPromptVersion(),
                        source.getTemperature(),
                        OffsetDateTime.now()
                ))
                .toList();
    }

    /**
     * basic hard 질문을 고를 때, 다양성까지 고려해서 점수를 다시 계산하는 함수
     * @param item
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @param selectedTags
     * @return
     */
    private double calculatePairScoreWithDiversity(
            GeneratedQuestion.Item item,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts,
            Set<String> selectedTags
    ) {
        double score = calculatePairScore(item, recentTitles, recentBodies, recentPrimaryTagCounts);

        if (selectedTags.contains(item.primaryTag())) {
            score -= 0.4;
        }

        return score;
    }

    /**
     * extra hard 질문을 고를 때, 다양성까지 고려해서 점수를 다시 계산하는 함수
     * @param item
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @param selectedTags
     * @return
     */
    private double calculateExtraHardScoreWithDiversity(
            GeneratedQuestion.Item item,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts,
            Set<String> selectedTags
    ) {
        double score = calculateExtraHardScore(
                item,
                recentTitles,
                recentBodies,
                recentPrimaryTagCounts
        );

        if (selectedTags.contains(item.primaryTag())) {
            score -= 0.3;
        }

        return score;
    }

    /**
     * 이전 면접 질문 조회
     * @param userId
     * @return
     */
    private List<InterviewQuestion> getInterviewQuestions(Long userId) {
        UserInterviewSetting byUserId = userInterviewSettingService.findByUserId(userId);
        List<Interview> interviews = interviewRepository.findTop30ByUserIdAndTrackOrderByCreatedAtDesc(userId,byUserId.getTrack());
        List<Long> interviewIds = interviews.stream()
                .map(Interview::getId)
                .toList();
        List<InterviewQuestion> historyInterview = interviewQuestionRepository.findTop30ByInterviewIdInAndPrimaryTagIsNotNullOrderByCreatedAtDesc(interviewIds);
        return historyInterview;
    }

    /**
     * 면접 질문 ai 호출 후 데이터 가공
     *
     * @param items
     * @param recentHistories
     * @param totalCount
     * @return
     */
    public List<SelectedQuestion> selectQuestions(
            List<GeneratedQuestion.Item> items,
            List<InterviewQuestion> recentHistories,
            int totalCount
    ) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        SelectionPolicy policy = resolvePolicy(totalCount);

        Set<String> recentTitles = recentHistories.stream()
                .map(h -> normalize(h.getTitle()))
                .collect(Collectors.toSet());

        Set<String> recentBodies = recentHistories.stream()
                .map(h -> normalize(h.getQuestionText()))
                .collect(Collectors.toSet());

        Map<String, Long> recentPrimaryTagCounts = recentHistories.stream()
                .filter(h -> h.getPrimaryTag() != null)
                .collect(Collectors.groupingBy(
                        InterviewQuestion::getPrimaryTag,
                        Collectors.counting()
                ));

        List<GeneratedQuestion.Item> selectedPairs = selectPairs(
                items,
                policy.pairCount(),
                recentTitles,
                recentBodies,
                recentPrimaryTagCounts
        );

        Set<GeneratedQuestion.Item> selectedPairSet = new HashSet<>(selectedPairs);

        List<GeneratedQuestion.Item> extraHardItems = selectExtraHardItems(
                items.stream()
                        .filter(item -> !selectedPairSet.contains(item))
                        .toList(),
                policy.extraHardCount(),
                selectedPairs,
                recentTitles,
                recentBodies,
                recentPrimaryTagCounts
        );

        List<SelectedQuestion> result = new ArrayList<>();

        for (GeneratedQuestion.Item pair : selectedPairs) {
            result.add(toSelectedBasicQuestion(pair));
            result.add(toSelectedHardQuestion(pair));
        }

        for (GeneratedQuestion.Item item : extraHardItems) {
            result.add(toSelectedHardQuestion(item));
        }

        return result;
    }

    /**
     * 면접 질문 갯수 정책
     *
     * @param totalCount
     * @return
     */
    private SelectionPolicy resolvePolicy(int totalCount) {
        return switch (totalCount) {
            case 3 -> new SelectionPolicy(1, 1);
            case 5 -> new SelectionPolicy(1, 3);
            case 7 -> new SelectionPolicy(2, 3);
            case 10 -> new SelectionPolicy(2, 6);
            default -> throw new IllegalArgumentException("Unsupported totalCount: " + totalCount);
        };
    }

    /**
     * 문자 정리
     * @param text
     * @return
     */
    private String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("[^a-z0-9가-힣 ]", "")
                .trim();
    }

    /**
     * 개념 질문 + 심화 질문 면접 선택 메서드
     * @param items
     * @param pairCount
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @return
     */
    private List<GeneratedQuestion.Item> selectPairs(
            List<GeneratedQuestion.Item> items,
            int pairCount,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts
    ) {
        List<GeneratedQuestion.Item> remaining = new ArrayList<>(items);
        List<GeneratedQuestion.Item> selected = new ArrayList<>();
        Set<String> selectedTags = new HashSet<>();

        int count = Math.min(pairCount, remaining.size());

        for (int i = 0; i < count; i++) {
            GeneratedQuestion.Item best = remaining.stream()
                    .max(Comparator.comparingDouble(item ->
                            calculatePairScoreWithDiversity(
                                    item,
                                    recentTitles,
                                    recentBodies,
                                    recentPrimaryTagCounts,
                                    selectedTags
                            )
                    ))
                    .orElseThrow();

            selected.add(best);
            selectedTags.add(best.primaryTag());
            remaining.remove(best);
        }

        return selected;
    }

    /**
     * 심화 질문 가져오기
     * @param candidates
     * @param extraHardCount
     * @param selectedPairs
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @return
     */
    private List<GeneratedQuestion.Item> selectExtraHardItems(
            List<GeneratedQuestion.Item> candidates,
            int extraHardCount,
            List<GeneratedQuestion.Item> selectedPairs,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts
    ) {
        List<GeneratedQuestion.Item> remaining = new ArrayList<>(candidates);
        List<GeneratedQuestion.Item> selected = new ArrayList<>();

        Set<String> selectedTags = selectedPairs.stream()
                .map(GeneratedQuestion.Item::primaryTag)
                .collect(Collectors.toSet());

        int count = Math.min(extraHardCount, remaining.size());

        for (int i = 0; i < count; i++) {
            GeneratedQuestion.Item best = remaining.stream()
                    .max(Comparator.comparingDouble(item ->
                            calculateExtraHardScoreWithDiversity(
                                    item,
                                    recentTitles,
                                    recentBodies,
                                    recentPrimaryTagCounts,
                                    selectedTags
                            )
                    ))
                    .orElseThrow();

            selected.add(best);
            selectedTags.add(best.primaryTag());
            remaining.remove(best);
        }

        return selected;
    }

    /**
     * 기본 + 심화 질문 변환 메서드
     * @param item
     * @return
     */
    private SelectedQuestion toSelectedBasicQuestion(GeneratedQuestion.Item item) {
        return new SelectedQuestion(
                item.basicQuestion().title(),
                item.basicQuestion().body(),
                item.primaryTag(),
                item.tags(),
                item.provider(),
                item.model(),
                item.promptVersion(),
                item.temperature()
        );
    }

    /**
     * 심화 질문 변환 메서드
     * @param item
     * @return
     */
    private SelectedQuestion toSelectedHardQuestion(GeneratedQuestion.Item item) {
        return new SelectedQuestion(
                item.hardQuestion().title(),
                item.hardQuestion().body(),
                item.primaryTag(),
                item.tags(),
                item.provider(),
                item.model(),
                item.promptVersion(),
                item.temperature()
        );
    }

    /**
     * 심화 면접 점수 메서드
     * @param item
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @return
     */
    private double calculateExtraHardScore(
            GeneratedQuestion.Item item,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts
    ) {
        double score = calculateQuestionScore(item.hardQuestion(), recentTitles, recentBodies);

        long tagCount = recentPrimaryTagCounts.getOrDefault(item.primaryTag(), 0L);
        score -= tagCount * 0.1;

        if (tagCount == 0) {
            score += 0.2;
        }

        return score;
    }

    /**
     * 면접 질문 가중치 점수 메서드
     * @param question
     * @param recentTitles
     * @param recentBodies
     * @return
     */
    private double calculateQuestionScore(
            Question question,
            Set<String> recentTitles,
            Set<String> recentBodies
    ) {
        double score = 1.0;

        String title = normalize(question.title());
        String body = normalize(question.body());

        if (recentTitles.contains(title)) {
            score -= 0.5;
        }

        if (recentBodies.contains(body)) {
            score -= 0.3;
        }

        return score;
    }

    /**
     * 기본 + 심화 질문 점수 가중치 메서드
     * @param item
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @return
     */
    private double calculatePairScore(
            GeneratedQuestion.Item item,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts
    ) {
        double basicScore = calculateQuestionScore(item.basicQuestion(), recentTitles, recentBodies);
        double hardScore = calculateQuestionScore(item.hardQuestion(), recentTitles, recentBodies);

        long tagCount = recentPrimaryTagCounts.getOrDefault(item.primaryTag(), 0L);

        double score = (basicScore * 0.4) + (hardScore * 0.6);
        score -= tagCount * 0.1;

        if (tagCount == 0) {
            score += 0.3;
        }

        return score;
    }

}