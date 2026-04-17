package com.mockio.support_service.questionboard.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_jpa.dto.PageDto;
import com.mockio.support_service.questionboard.Mapper.QuestionBoardMapper;
import com.mockio.support_service.questionboard.client.CoreToQuestionBoardClient;
import com.mockio.support_service.questionboard.domain.QuestionBoard;
import com.mockio.support_service.questionboard.domain.QuestionBoardItem;
import com.mockio.support_service.questionboard.dto.internal.response.InterviewListResponse;
import com.mockio.support_service.questionboard.dto.internal.response.QuestionAnswerResponse;
import com.mockio.support_service.questionboard.dto.internal.response.InternalQuestionBoardDetailResponse;
import com.mockio.support_service.questionboard.dto.internal.response.UserInfoResponse;
import com.mockio.support_service.questionboard.dto.request.*;
import com.mockio.support_service.questionboard.dto.response.*;
import com.mockio.support_service.questionboard.repository.QuestionBoardQueryDslRepository;
import com.mockio.support_service.questionboard.repository.QuestionBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mockio.common_core.constant.CommonErrorEnum.*;
import static com.mockio.support_service.questionboard.constant.QuestionboardErrorEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardQueryDslRepository questionBoardQueryDslRepository;
    private final CoreToQuestionBoardClient coreClient;

    /**
     * 면접 공유 게시판 등록 데이터 조회
     * @param userId
     */
    public QuestionBoardListResponse createQuestionBoardSetting(Long userId, Long interviewId,Long questionId) {

        //interview 정보 조회 하기.
        InterviewListResponse interviewList = coreClient.getInterviewList(userId);
        Long selectedInterviewId = null;
        if(interviewId != null) {
            selectedInterviewId = interviewList.interviews().stream()
                    .filter(item -> Objects.equals(item.id(), interviewId))
                    .map(InterviewListResponse.Item::id)
                    .findFirst()
                    .orElse(null);
        } else {
            //면접 정보가 있다면 면접 첫번째 질문 목록 조회
            if(!interviewList.interviews().isEmpty()) {
                selectedInterviewId = interviewList.interviews().getFirst().id();

            } else {
                 throw new CustomApiException(
                         ERR_000.getHttpStatus(),
                         ERR_000,
                         ERR_000.getMessage()
                 );
            }
        }

        QuestionAnswerResponse interviewQuestionAnswerList = coreClient.getInterviewQuestionAnswerList(userId, selectedInterviewId);
        Long selectedQuestionId = null;
        if(questionId!=null) {
            selectedQuestionId = interviewQuestionAnswerList.questions().stream()
                    .filter(question -> question.id().equals(questionId))
                    .map(QuestionAnswerResponse.QuestionItem::id)
                    .findFirst()
                    .orElse(null);
        }
        return  QuestionBoardMapper.fromQuestionBoardList(selectedInterviewId, interviewList, interviewQuestionAnswerList,selectedQuestionId);
    }

    /**
     * 면접 공유 게시판 게시글 등록
     * @param userId
     * @param req
     */
    public QuestionBoardCreateResponse createQuestionBoard(Long userId, QuestionBoardCreateRequest req) {
        //사용자 닉네임 조회
        UserInfoResponse userInfoResponse = coreClient.userDetail(userId);

        //면접 정보 조회
        InternalQuestionBoardCreateRequest internalQuestionBoardCreateRequest = new InternalQuestionBoardCreateRequest(
                req.title(),
                req.content(),
                req.interview().stream()
                        .map(item -> new InternalQuestionBoardCreateRequest.Item(
                                item.interviewId(),
                                item.questionId()
                        ))
                        .toList(),
                userId
        );

        InternalQuestionBoardDetailResponse questionBoardDetail = coreClient.getQuestionBoardDetail(internalQuestionBoardCreateRequest);

        //저장
        QuestionBoard questionBoard = QuestionBoard.createQuestionBoard(
                questionBoardDetail.track(),
                userId,
                userInfoResponse.nickname(),
                req.title(),
                req.content()
        );
        QuestionBoardItem questionBoardItem = QuestionBoardItem.createQuestionBoardItem(
                questionBoardDetail.interviewId(),
                questionBoardDetail.questionId(),
                questionBoardDetail.seq(),
                questionBoardDetail.questionText(),
                questionBoardDetail.answerId(),
                questionBoardDetail.answerText(),
                questionBoardDetail.score(),
                questionBoardDetail.summary(),
                req.interview().getFirst().scoreVisible(),
                req.interview().getFirst().aiFeedbackVisible()
        );
        questionBoard.addItem(
                questionBoardItem
        );

        questionBoardItem.replaceTags(req.interview().getFirst().tags()
                .stream()
                .collect(Collectors.toSet()));

        QuestionBoard save = questionBoardRepository.save(questionBoard);
        return new QuestionBoardCreateResponse(
                save.getId()
        );

    }

    public PageDto<QuestionBoardDslListResponse> getQuestionBoardList(QuestionBoardListRequest req) {
        return PageDto.of(
                questionBoardQueryDslRepository.findQuestionBoardList(req),
                it -> {
                    if (!Boolean.TRUE.equals(it.getScoreVisible())) {
                        it.setScore(null);
                    }
                    return it;
                }
        );
    }

    public QuestionBoardDetailResponse getPublicQuestionBoardDetail(Long questionBoardId, Long userId) {
        QuestionBoard questionBoard = questionBoardRepository.findByIdAndDeleted(questionBoardId, false)
                .orElseThrow(
                        ()-> new CustomApiException(
                                QUESTION_BOARD_NOT_FOUND.getHttpStatus(),
                                QUESTION_BOARD_NOT_FOUND,
                                QUESTION_BOARD_NOT_FOUND.getMessage()
                        )
                );
        List<QuestionBoardDetailResponse.Item> list = questionBoard.getItems().stream()
                .map(boardItem ->
                        new QuestionBoardDetailResponse.Item(
                                boardItem.getId(),
                                boardItem.getQuestionText(),
                                boardItem.getAnswerText(),
                                boardItem.getQuestionSeq(),
                                boardItem.isScoreVisible() ? boardItem.getScore() : null,
                                boardItem.isAiFeedbackVisible() ? boardItem.getAiFeedbackSummaryText() : null,
                                boardItem.isScoreVisible(),
                                boardItem.isAiFeedbackVisible(),
                                boardItem.getTags()
                        )
                ).toList();
        questionBoard.addReadCount();
        return new QuestionBoardDetailResponse(
                questionBoard.getId(),
                questionBoard.getTitle(),
                questionBoard.getContent(),
                questionBoard.getNickname(),
                userId == null ? false : userId.equals(questionBoard.getUserId()),
                questionBoard.getCreatedAt(),
                questionBoard.getReadCount(),
                list
        );
    }

    /**
     * 면접 공유 게시판 게시글 사용자 인증 조회
     * @param userId
     * @param questionBoardId
     * @return
     */
    public QuestionBoardUpdateDetailResponse getQuestionBoardDetail(Long userId , Long questionBoardId) {
        QuestionBoard questionBoard = findQuestionBoard(questionBoardId, userId);

        QuestionBoardItem boardItem = questionBoard.getItems().getFirst();
        return new QuestionBoardUpdateDetailResponse(
                boardItem.getInterviewId(),
                boardItem.getQuestionId(),
                questionBoard.getTitle(),
                questionBoard.getContent(),
                boardItem.isScoreVisible(),
                boardItem.isAiFeedbackVisible(),
                boardItem.getTags()
        );

    }

    /**
     * 면접 공유 게시판 게시글 수정
     * @param userId
     * @param req
     */
    public void updateQuestionBoard(Long userId, QuestionBoardUpdateRequest req) {
        QuestionBoard questionBoard = findQuestionBoard(req.boardId(), userId);

        QuestionBoardItem boardItem = questionBoard.getItems().getFirst();
        questionBoard.applyPatch(
                req.title(),
                req.content(),
                boardItem,
                req.tags(),
                req.scoreVisible(),
                req.aiFeedbackVisible()
        );

    }

    /**
     * 면접 공유 게시판 게시글 삭제
     * @param userId
     * @param req
     */
    public void deleteQuestionBoard(Long userId , QuestionBoardDeleteRequest req) {
        QuestionBoard questionBoard = findQuestionBoard(req.boardId(), userId);
        questionBoard.softDelete(userId);
    }

    @NonNull
    private QuestionBoard findQuestionBoard(Long boardId, Long userId) {
        return questionBoardRepository.findByIdAndUserIdAndDeleted(boardId, userId, false)
                .orElseThrow(
                        () -> new CustomApiException(
                                QUESTION_BOARD_NOT_FORBIDDEN.getHttpStatus(),
                                QUESTION_BOARD_NOT_FORBIDDEN,
                                QUESTION_BOARD_NOT_FORBIDDEN.getMessage()
                        )
                );
    }

    /**
     * 게시판 타입 체크
     * @param boardId
     * @return
     */
    public boolean findQuestionBoardTypeCheck(Long boardId) {
       return questionBoardRepository.existsByIdAndDeleted(boardId,false);
    }

    /**
     * 댓글 서비스에서 사용 하므로
     * 리턴 entity 괜찮음
     * @param boardId
     * @return
     */
    public QuestionBoard findQuestionBoard(Long boardId) {
        return  questionBoardRepository.findByIdAndDeleted(boardId, false)
                .orElseThrow(
                        () -> new CustomApiException(
                                QUESTION_BOARD_NOT_FORBIDDEN.getHttpStatus(),
                                QUESTION_BOARD_NOT_FORBIDDEN,
                                QUESTION_BOARD_NOT_FORBIDDEN.getMessage()
                        )
                );
    }
}
