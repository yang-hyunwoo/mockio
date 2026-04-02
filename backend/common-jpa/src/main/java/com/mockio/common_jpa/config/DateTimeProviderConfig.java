package com.mockio.common_jpa.config;

/**
 * JPA Auditing에서 사용할 DateTimeProvider 설정 클래스.
 *
 * {offSetDateTimeProvider} Bean을 등록하여
 * 엔티티의 생성일/수정일 등에 사용되는 시간 값을 {OffsetDateTime} 기준으로 제공한다.
 *
 * 타임존 정보를 포함한 시간을 사용함으로써,
 * 서버 환경에 관계없이 일관된 시간 처리를 보장한다.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
public class DateTimeProviderConfig {

    @Bean("offSetDateTimeProvider")
    public DateTimeProvider offSetDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

}