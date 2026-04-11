package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.Mapper.InterviewQuestionMapper;
import com.mockio.core_service.interview.constant.QuestionType;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewKeyword;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.request.InterviewGenerateContext;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.dto.response.SelectedQuestion;
import com.mockio.core_service.interview.repository.InterviewQuestionRepository;
import com.mockio.core_service.interview.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static com.mockio.common_ai_contractor.constant.InterviewStatus.*;

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

        List<String> interviewKeywordList = userInterviewSettingService.findByUserId(userId).getKeywords()
                .stream()
                .map(InterviewKeyword::getKeyword)
                .toList();
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
                interview.getCount(),
                interviewKeywordList

        );
    }

    @Transactional
    public void completeGenerate(Long interviewId, Long userId, List<SelectedQuestion> selectedQuestions) {
        Interview interview = interviewRepository.findByIdAndUserIdForUpdate(interviewId, userId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));

        if (interview.isQuestionGenerated()) {
            return;
        }

        List<InterviewQuestion> entities = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();
        for (int i = 0; i < selectedQuestions.size(); i++) {
            SelectedQuestion q = selectedQuestions.get(i);
            int order = (i + 1) * 10;
            entities.add(InterviewQuestion.createInterviewQuestion(
                    interview,
                    order,
                    q.question().title(),
                    q.primaryTag(),
                    q.tags(),
                    q.question().body(),
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
        savedRetryInterview.connectRetryChain(sourceInterview);
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
        return interviewQuestionRepository.findTop30ByInterviewIdInAndPrimaryTagIsNotNullOrderByCreatedAtDesc(interviewIds);
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
     * 기본+심화  질문 변환 메서드
     * @param item
     * @return
     */
    private SelectedQuestion toSelectedQuestion(GeneratedQuestion.Item item) {
        return new SelectedQuestion(
                item.question(),
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
        double score = calculateQuestionScore(item.question(), recentTitles, recentBodies);

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
        double basicScore = calculateQuestionScore(item.question(), recentTitles, recentBodies);
        double hardScore = calculateQuestionScore(item.question(), recentTitles, recentBodies);

        long tagCount = recentPrimaryTagCounts.getOrDefault(item.primaryTag(), 0L);

        double score = (basicScore * 0.4) + (hardScore * 0.6);
        score -= tagCount * 0.1;

        if (tagCount == 0) {
            score += 0.3;
        }

        return score;
    }

    /**
     * 기본 개념 질문  고르기
     * @param items
     * @param userId
     * @param basicCount
     * @return
     */
    @Transactional(readOnly = true)
    public List<SelectedQuestion> selectBasicQuestions(
            List<GeneratedQuestion.Item> items,
            Long userId,
            int basicCount
    ) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        List<InterviewQuestion> recentHistories = getInterviewQuestions(userId);

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

        List<GeneratedQuestion.Item> remaining = new ArrayList<>(items);
        List<SelectedQuestion> selected = new ArrayList<>();
        Set<String> selectedTags = new HashSet<>();

        int count = Math.min(basicCount, remaining.size());

        for (int i = 0; i < count; i++) {
            GeneratedQuestion.Item best = remaining.stream()
                    .max(Comparator.comparingDouble(item ->
                            calculateBasicScoreWithDiversity(
                                    item,
                                    recentTitles,
                                    recentBodies,
                                    recentPrimaryTagCounts,
                                    selectedTags
                            )
                    ))
                    .orElseThrow();

            selected.add(toSelectedQuestion(best));
            selectedTags.add(best.primaryTag());
            remaining.remove(best);
        }

        return selected;
    }

    /**
     * 기본 질문 점수 가중치 메서드
     * @param item
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @param selectedTags
     * @return
     */
    private double calculateBasicScoreWithDiversity(
            GeneratedQuestion.Item item,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts,
            Set<String> selectedTags
    ) {
        double score = calculateQuestionScore(item.question(), recentTitles, recentBodies);

        long tagCount = recentPrimaryTagCounts.getOrDefault(item.primaryTag(), 0L);
        score -= tagCount * 0.1;

        if (tagCount == 0) {
            score += 0.3;
        }

        if (selectedTags.contains(item.primaryTag())) {
            score -= 0.4;
        }

        return score;
    }

    /**
     * 기본 질문에서 넘어온 심화 질문 선택 메서드
     * @param candidates
     * @param selectedBasics
     * @param userId
     * @param linkedHardCount
     * @return
     */
    @Transactional(readOnly = true)
    public List<SelectedQuestion> selectLinkedHardQuestions(
            List<GeneratedQuestion.Item> candidates,
            List<SelectedQuestion> selectedBasics,
            Long userId,
            int linkedHardCount
    ) {
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        List<InterviewQuestion> recentHistories = getInterviewQuestions(userId);

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

        List<SelectedQuestion> result = new ArrayList<>();

        for (SelectedQuestion basic : selectedBasics) {
            GeneratedQuestion.Item best = candidates.stream()
                    .filter(item -> basic.question().title().equals(item.question().baseOnTitle()))
                    .max(Comparator.comparingDouble(item ->
                            calculateHardScoreWithDiversity(
                                    item,
                                    recentTitles,
                                    recentBodies,
                                    recentPrimaryTagCounts,
                                    result.stream()
                                            .map(SelectedQuestion::primaryTag)
                                            .collect(Collectors.toSet())
                            )
                    ))
                    .orElse(null);

            if (best != null) {
                result.add(toSelectedQuestion(best));
            }

            if (result.size() >= linkedHardCount) {
                break;
            }
        }

        return result;
    }

    /**
     * 심화 질문 선택 메서드 (기본 -> 심화 제외)
     * @param candidates
     * @param userId
     * @param hardCount
     * @param excludeTags
     * @return
     */
    @Transactional(readOnly = true)
    public List<SelectedQuestion> selectHardQuestions(
            List<GeneratedQuestion.Item> candidates,
            Long userId,
            int hardCount,
            Set<String> excludeTags
    ) {
        List<GeneratedQuestion.Item> remaining = candidates.stream()
                .filter(item -> "HARD".equals(item.question().questionType()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (remaining.isEmpty()) {
            return List.of();
        }

        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        List<InterviewQuestion> recentHistories = getInterviewQuestions(userId);

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

        List<SelectedQuestion> selected = new ArrayList<>();
        Set<String> selectedTags = new HashSet<>(excludeTags);

        int count = Math.min(hardCount, remaining.size());

        for (int i = 0; i < count; i++) {
            GeneratedQuestion.Item best = remaining.stream()
                    .max(Comparator.comparingDouble(item ->
                            calculateHardScoreWithDiversity(
                                    item,
                                    recentTitles,
                                    recentBodies,
                                    recentPrimaryTagCounts,
                                    selectedTags
                            )
                    ))
                    .orElseThrow();

            selected.add(toSelectedQuestion(best));
            selectedTags.add(best.primaryTag());
            remaining.remove(best);
        }

        return selected;
    }


    /**
     * 심화 질문 가중침 점수 메서드
     * @param item
     * @param recentTitles
     * @param recentBodies
     * @param recentPrimaryTagCounts
     * @param selectedTags
     * @return
     */
    private double calculateHardScoreWithDiversity(
            GeneratedQuestion.Item item,
            Set<String> recentTitles,
            Set<String> recentBodies,
            Map<String, Long> recentPrimaryTagCounts,
            Set<String> selectedTags
    ) {
        double score = calculateQuestionScore(item.question(), recentTitles, recentBodies);

        long tagCount = recentPrimaryTagCounts.getOrDefault(item.primaryTag(), 0L);
        score -= tagCount * 0.1;

        if (tagCount == 0) {
            score += 0.2;
        }

        if (selectedTags.contains(item.primaryTag())) {
            score -= 0.3;
        }

        return score;
    }

}
