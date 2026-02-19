package com.mockio.common_kafka.constant.error;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KafkaErrorCode implements ErrorCode {
    TransientBusinessException(500,"TransientBusinessException","kafka 중복 데이터 저장"),
    NonRetryableEventException(500,"NonRetryableEventException","Kafka 재시도 안함"),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

}
