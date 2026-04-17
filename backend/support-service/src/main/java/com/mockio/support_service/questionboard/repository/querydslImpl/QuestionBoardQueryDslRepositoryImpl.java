package com.mockio.support_service.questionboard.repository.querydslImpl;

import com.mockio.support_service.questionboard.domain.QQuestionBoard;
import com.mockio.support_service.questionboard.domain.QQuestionBoardItem;
import com.mockio.support_service.questionboard.domain.QuestionBoardItem;
import com.mockio.support_service.questionboard.dto.request.QuestionBoardListRequest;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardDslListResponse;
import com.mockio.support_service.questionboard.dto.response.QuestionBoardDslListRow;
import com.mockio.support_service.questionboard.repository.QuestionBoardQueryDslRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;


@RequiredArgsConstructor
@Repository
public class QuestionBoardQueryDslRepositoryImpl implements QuestionBoardQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    private final QQuestionBoard q = QQuestionBoard.questionBoard;
    private final QQuestionBoardItem qi = QQuestionBoardItem.questionBoardItem;

    @Override
    public Page<QuestionBoardDslListResponse> findQuestionBoardList(QuestionBoardListRequest req) {

        Pageable pageable = PageRequest.of(req.page(), req.size());

        List<QuestionBoardDslListRow> rows = queryFactory
                .select(Projections.constructor(
                        QuestionBoardDslListRow.class,
                        q.id,
                        qi.id,
                        q.title,
                        q.nickname,
                        q.track,
                        q.readCount,
                        qi.questionText,
                        qi.answerText,
                        qi.score,
                        qi.scoreVisible,
                        qi.aiFeedbackVisible,
                        q.createdAt
                ))
                .from(q)
                .join(q.items, qi)
                .where(
                        qi.displayOrder.eq(1),
                        keywordContains(req.value()),
                        trackEq(req.track()),
                        scoreVisible(req.scoreVisible()),
                        q.deleted.isFalse()
                )
                .orderBy(q.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(q.id.countDistinct())
                .from(q)
                .join(q.items, qi)
                .where(
                        qi.displayOrder.eq(1),
                        keywordContains(req.value()),
                        trackEq(req.track()),
                        scoreVisible(req.scoreVisible()),
                        q.deleted.isFalse()
                )
                .fetchOne();

        List<Long> itemIds = rows.stream()
                .map(QuestionBoardDslListRow::getBoardItemId)
                .toList();

        Map<Long, Set<String>> tagMap = loadTags(itemIds);

        List<QuestionBoardDslListResponse> content = rows.stream()
                .map(row -> new QuestionBoardDslListResponse(
                        row.getBoardId(),
                        row.getBoardItemId(),
                        row.getTitle(),
                        row.getNickname(),
                        row.getTrack(),
                        row.getQuestion(),
                        row.getAnswerPreview(),
                        row.getReadCount(),
                        row.getScore(),
                        row.getScoreVisible(),
                        row.getFeedbackVisible(),
                        tagMap.getOrDefault(row.getBoardItemId(), Set.of()),
                        row.getCreatedAt()
                ))
                .toList();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }

    private Map<Long, Set<String>> loadTags(List<Long> itemIds) {
        if (itemIds.isEmpty()) {
            return Map.of();
        }

        List<QuestionBoardItem> items = queryFactory
                .selectFrom(qi)
                .where(qi.id.in(itemIds))
                .fetch();

        return items.stream()
                .collect(Collectors.toMap(
                        QuestionBoardItem::getId,
                        item -> new LinkedHashSet<>(item.getTags())
                ));
    }

    private BooleanExpression keywordContains(String keyword) {
        if (!hasText(keyword)) {
            return null;
        }

        return q.title.containsIgnoreCase(keyword)
                .or(qi.questionText.containsIgnoreCase(keyword))
                .or(qi.answerText.containsIgnoreCase(keyword))
                .or(q.nickname.contains(keyword))
                .or(qi.tags.any().containsIgnoreCase(keyword));
    }

    private BooleanExpression trackEq(String track) {
        if (!hasText(track) || "ALL".equals(track)) {
            return null;
        }
        return q.track.eq(track);
    }

    private BooleanExpression scoreVisible(Boolean scoreOnly) {
        return Boolean.TRUE.equals(scoreOnly) ? qi.scoreVisible.isTrue() : null;
    }
}
