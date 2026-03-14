package com.mockio.notification_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
