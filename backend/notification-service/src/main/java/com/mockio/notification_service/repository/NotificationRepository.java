package com.mockio.notification_service.repository;

import com.mockio.notification_service.constant.NotificationType;
import com.mockio.notification_service.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findBySourceEventId(String sourceEventId);

    Optional<Notification> findByUserIdAndTypeAndReferenceTypeAndReferenceId(
            Long userId,
            NotificationType type,
            String referenceType,
            Long referenceId
    );

    @Query("""
            SELECT n
            FROM Notification n
            WHERE n.userId = :userId
            ORDER BY n.isRead ASC, n.createdAt DESC
            """)
    List<Notification> findTop10ByUserId(@Param("userId") Long userId, Pageable pageable);

    int countByUserIdAndIsReadFalse(Long userId);

    Optional<Notification> findByIdAndUserId(Long id,Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update Notification n
                   set n.isRead = true
                     , n.readAt = CURRENT_TIMESTAMP
                 where n.userId = :userId
                   and n.isRead = false
            """)
    int readAllByUserId(@Param("userId") Long userId);
}
