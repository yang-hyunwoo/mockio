package com.mockio.support_service.faq.dto.request;

import com.mockio.common_core.annotation.Sanitize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.Step1;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FaqReqDto {

    @Schema(description = "찾고자 하는 제목" , example = "출석은 어떻게")
    @Sanitize(groups = Step1.class)
    private String question;

    @Override
    public String toString() {
        return "FaqReqDto{" +
                "question='" + question + '\'' +
                '}';
    }

}
