package com.mockio.support_service.questionboard.repository;

import com.mockio.support_service.questionboard.domain.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {

    Optional<QuestionBoard> findByIdAndDeleted(Long questionBoardId, boolean deleted);

    boolean existsByIdAndDeleted(Long questionBoardId, boolean deleted);

    Optional<QuestionBoard> findByIdAndUserIdAndDeleted(Long questionBoardId,Long userId, boolean deleted);

}
