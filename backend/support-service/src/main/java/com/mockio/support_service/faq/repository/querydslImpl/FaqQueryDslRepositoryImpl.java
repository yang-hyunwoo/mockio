package com.mockio.support_service.faq.repository.querydslImpl;

import com.mockio.support_service.faq.domain.FaqBoard;
import com.mockio.support_service.faq.domain.QFaqBoard;
import com.mockio.support_service.faq.dto.request.FaqReqDto;
import com.mockio.support_service.faq.repository.FaqQueryDslRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class FaqQueryDslRepositoryImpl implements FaqQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    QFaqBoard f = QFaqBoard.faqBoard;

    /**
     * faq 페이징 조회
     *
     * @param faqReqDto 검색 조건
     * @return faq 페이지 객체
     */
    @Override
    public List<FaqBoard> findFaqList(FaqReqDto faqReqDto
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        faqCondition(faqReqDto, builder);



        return queryFactory.selectFrom(f)
                .where(builder)
                .orderBy(f.sort.asc())
                .fetch();

    }

    private void faqCondition(FaqReqDto faqReqDto,
                              BooleanBuilder builder
    ) {
        builder.and(f.visible.isTrue());
        builder.and(f.deleted.isFalse());

//        if (faqReqDto.getFaqType() != null) {
//            FaqType typeEnum = FaqType.valueOf(faqReqDto.getFaqType());
//            builder.and(f.faqType.eq(typeEnum));
//        }

//        if (faqReqDto.getQuestion() != null && !faqReqDto.getQuestion().isBlank()) {
//            builder.and(f.question.value.containsIgnoreCase(faqReqDto.getQuestion()));
//        }
    }

}
