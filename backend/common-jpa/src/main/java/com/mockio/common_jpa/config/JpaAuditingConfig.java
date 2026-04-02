package com.mockio.common_jpa.config;

/**
 * JPA Auditing 설정 클래스.
 *
 * {org.springframework.data.jpa.repository.config.EnableJpaAuditing}을 통해
 * 생성일, 수정일 등의 Auditing 기능을 활성화한다.
 *
 * {offSetDateTimeProvider}를 사용하여 Auditing 시간 값을 커스터마이징한다.
 *
 * {jpa.auditing.enabled} 설정값이 true일 때만 활성화되며,
 * 별도 설정이 없을 경우 기본적으로 활성화된다.
 */

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "offSetDateTimeProvider")
@ConditionalOnProperty(name = "jpa.auditing.enabled", havingValue = "true", matchIfMissing = true)
public class JpaAuditingConfig {
}