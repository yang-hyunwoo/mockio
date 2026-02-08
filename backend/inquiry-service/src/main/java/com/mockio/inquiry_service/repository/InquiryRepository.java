package com.mockio.inquiry_service.repository;

import com.mockio.inquiry_service.domain.InquiryBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<InquiryBoard, Long> {

    /**
     * 멤버가 작성한 qna 리스트 조회
     *
     * @param userId 멤버 id
     * @return 멤버가 작성한 qna 리스트 객체
     */
    @Query(value = """
                    SELECT * FROM qna_board
                    WHERE question_user_id = :userId
                    ORDER BY id desc
            """, nativeQuery = true)
    List<InquiryBoard> findByQuestionUserIdNative(@Param("userId") String userId);

}
