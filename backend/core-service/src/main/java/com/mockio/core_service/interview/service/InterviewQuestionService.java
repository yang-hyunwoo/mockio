package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.generator.question.*;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.dto.request.InterviewGenerateContext;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.request.StartInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.dto.response.SelectedQuestion;
import com.mockio.core_service.interview.dto.response.SelectionPolicy;
import com.mockio.core_service.interview.forward.ai.AIServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.AI_SERVICE_FAILED;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final AIServiceClient aiServiceClient;
    private final InterviewCreateService interviewCreateService;
    private final InterviewQuestionTxService interviewQuestionTxService;

    public InterviewQuestionReadResponse startInterview(Long userId, StartInterviewRequest request) {
        Long interviewId = interviewCreateService.generateInterview(userId, request);
        return generateAndSaveQuestions(interviewId, userId);
    }

    public InterviewQuestionReadResponse generateAndSaveQuestions(Long interviewId, Long userId) {
        InterviewGenerateContext context = interviewQuestionTxService.prepareGenerate(interviewId, userId);
        List<SelectedQuestion> selectQuestionList = null;
        if (context.alreadyGenerated()) {
            return interviewQuestionTxService.getQuestions(interviewId, userId);
        }

        GeneratedQuestion generatedQuestion;
        try {

            int basicCount = resolvePolicy(context.count()).basicCount();
            int basicCountAdd = basicCount + 1;
            int hardLinkCount = resolvePolicy(context.count()).linkedHardCount();
            int hardCount = resolvePolicy(context.count()).hardCount();
            GenerateBasicQuestionCommand cmd = new GenerateBasicQuestionCommand(
                    userId,
                    context.track(),
                    context.difficulty(),
                    context.interviewMode(),
                    context.answerTimeSeconds(),
                    basicCountAdd,
                    context.interviewKeyword()
            );

            //1. 기본 개념 질문 생성
            generatedQuestion = aiServiceClient.generateBasicQuestions(cmd);
            List<SelectedQuestion> selectBasicQuestionList = interviewQuestionTxService.selectBasicQuestions(
                    generatedQuestion.questions(),
                    userId,
                    basicCount
            );

            GenerateHardQuestionCommand linkedHardCmd =
                    new GenerateHardQuestionCommand(
                            userId,
                            context.track(),
                            context.difficulty(),
                            context.interviewKeyword(),
                            hardLinkCount,
                            context.count()-1, //심화질문은 후보군 1개 제외
                            selectBasicQuestionList.stream()
                                    .map(q -> new BaseQuestionContext(
                                            q.question().title(),
                                            q.question().body(),
                                            q.primaryTag()
                                    ))
                                    .toList()
                    );

            // 심화 질문 생성
            GeneratedQuestion generatedHardQuestion = aiServiceClient.generateHardQuestions(linkedHardCmd);

            List<SelectedQuestion> selectLinkedHardQuestionList = interviewQuestionTxService.selectLinkedHardQuestions(
                    generatedHardQuestion.questions(),
                    selectBasicQuestionList,
                    userId,
                    hardLinkCount
            );


            List<SelectedQuestion> selecteHardQuestionList = interviewQuestionTxService.selectHardQuestions(
                    generatedHardQuestion.questions(),
                    userId,
                    hardCount,
                    generatedHardQuestion.questions().stream()
                            .map(GeneratedQuestion.Item::primaryTag)
                            .collect(Collectors.toSet())
            );

            selectQuestionList = composeFinalQuestions(selectBasicQuestionList,
                    selectLinkedHardQuestionList,
                    selecteHardQuestionList);
        } catch (Exception e) {
            interviewQuestionTxService.failGenerate(interviewId, userId, e.getMessage());
            throw new CustomApiException(
                    AI_SERVICE_FAILED.getHttpStatus(),
                    AI_SERVICE_FAILED,
                    AI_SERVICE_FAILED.getMessage()
            );
        }

        interviewQuestionTxService.completeGenerate(interviewId, userId, selectQuestionList);
        return interviewQuestionTxService.getQuestions(interviewId, userId);
    }

    public InterviewQuestionReadResponse retryInterview(Long userId, RetryInterviewRequest request) {
        return interviewQuestionTxService.retryInterview(userId, request);
    }

    /**
     * 면접 질문 갯수 정책
     *
     * @param totalCount
     * @return
     */
    private SelectionPolicy resolvePolicy(int totalCount) {
        return switch (totalCount) {
            case 3 -> new SelectionPolicy(1, 1, 1);
            case 5 -> new SelectionPolicy(1, 1, 3);
            case 7 -> new SelectionPolicy(2, 2, 3);
            case 10 -> new SelectionPolicy(2, 2, 6);
            default -> throw new IllegalArgumentException("Unsupported totalCount: " + totalCount);
        };
    }

    private List<SelectedQuestion> composeFinalQuestions(
            List<SelectedQuestion> basics,
            List<SelectedQuestion> linkedHards,
            List<SelectedQuestion> hards
    ) {
        List<SelectedQuestion> result = new ArrayList<>();

        Map<String, List<SelectedQuestion>> linkedHardMap = linkedHards.stream()
                .filter(q -> q.question() != null && q.question().baseOnTitle() != null)
                .collect(Collectors.groupingBy(q -> q.question().baseOnTitle()));

        for (SelectedQuestion basic : basics) {
            result.add(basic);

            String basicTitle = basic.question().title();
            List<SelectedQuestion> matchedLinkedHards = linkedHardMap.getOrDefault(basicTitle, List.of());

            result.addAll(matchedLinkedHards);
        }

        result.addAll(hards);

        return result;
    }

}
