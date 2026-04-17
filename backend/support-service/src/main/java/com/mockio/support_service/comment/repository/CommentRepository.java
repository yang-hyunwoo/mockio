package com.mockio.support_service.comment.repository;

import com.mockio.support_service.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment , Long> {

    Page<Comment> findByBoardTypeAndBoardIdAndParentIdIsNullOrderByIdDesc(
            String boardType,
            Long boardId,
            Pageable pageable
    );

    List<Comment> findByParentIdInOrderByCreatedAtAsc(List<Long> parentIds);

    Optional<Comment> findByIdAndBoardTypeAndDepthAndUserIdAndDeleted(
            Long id,
            String boardType,
            int depth,
            Long userId,
            boolean deleted
    );

    Optional<Comment> findByIdAndBoardTypeAndDepthAndUserIdAndParentIdAndDeleted(
            Long id,
            String boardType,
            int depth,
            Long userId,
            Long parentId,
            boolean deleted
    );

    Optional<Comment> findByIdAndDeleted(Long parentId, boolean deleted);
}
