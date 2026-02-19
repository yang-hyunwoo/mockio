package com.mockio.common_jpa.domain.vo.abstarct;

import com.mockio.common_core.exception.CustomApiException;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_000;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractContent {

    @Column(columnDefinition = "TEXT")
    private String value;

    protected AbstractContent(String value) {
        this.value = value;
    }
    protected void validateRequired(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new CustomApiException(ERR_000.getHttpStatus(), ERR_000, ERR_000.getMessage());
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
