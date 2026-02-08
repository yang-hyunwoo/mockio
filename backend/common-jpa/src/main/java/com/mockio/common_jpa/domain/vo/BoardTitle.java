package com.mockio.common_jpa.domain.vo;

import com.mockio.common_jpa.domain.vo.abstarct.AbstractString;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardTitle extends AbstractString {

    public BoardTitle(String value) {
        super(value);
        validateLength(value,200);
        validateEmpty(value);
    }

    public static BoardTitle of(String value) {
        return new BoardTitle(value);
    }

}
