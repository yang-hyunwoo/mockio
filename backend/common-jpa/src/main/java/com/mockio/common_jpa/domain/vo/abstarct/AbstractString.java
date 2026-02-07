package com.mockio.common_jpa.domain.vo.abstarct;

import ch.qos.logback.core.util.StringUtil;
import com.mockio.common_core.exception.CustomApiException;
import com.querydsl.core.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mockio.common_core.constant.CommonErrorEnum.*;


@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractString {

    @Column(length = 255)
    private String value;

    protected AbstractString(String value) {
        this.value = value;
    }

    /**
     * length 유효성 검사
     * @param value
     * @param maxLength
     */
    protected void validateLength(String value, int maxLength) {
        if (value.length() > maxLength) {
            throw new CustomApiException(ERR_002.getHttpStatus(), ERR_002,ERR_002.getMessage());
        }
    }

    //빈값 유효성 검사
    protected void  validateEmpty(String value) {
        if (StringUtils.isNullOrEmpty(value)) {
            throw new CustomApiException(ERR_002.getHttpStatus(), ERR_002,ERR_002.getMessage());
        }
    }

    @Override
    public String toString() {
        return "AbstractString{" +
                "value='" + value + '\'' +
                '}';
    }

}
