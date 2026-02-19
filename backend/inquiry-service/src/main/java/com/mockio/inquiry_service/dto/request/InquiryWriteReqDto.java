package com.mockio.inquiry_service.dto.request;

import com.mockio.common_core.annotation.EnumValidation;
import com.mockio.common_core.annotation.Sanitize;
import com.mockio.common_core.annotation.otherValidator.ValidationMode;
import com.mockio.common_core.constant.ValidationType;
import com.mockio.inquiry_service.constant.InquiryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ValidationMode(ValidationType.SINGLE)
@Builder
public class InquiryWriteReqDto {

    @Schema(description = "문의 유형", example = "SERVICE")
    @NotNull(message = "{inquiry.type}",groups = Step1.class)
    @EnumValidation(enumClass = InquiryType.class, message = "{inquiry.type}", groups = Step1.class)
    private String qnaType;

    @Schema(description = "문의 제목", example = "문의 제목 입니다.")
    @NotBlank(message = "{inquiry.title}", groups = Step2.class)
    @Length(max = 500, message = "{inquiry.titleMaxLength}", groups = Step2.class)
    @Sanitize(groups = Step2.class)
    private String questionTitle;

    @Schema(description = "문의 내용" , example = "문의 내용 입니다.")
    @NotBlank(message = "{inquiry.content}",groups = Step3.class)
    @Sanitize(groups = Step3.class)
    private String questionContent;

    @Override
    public String toString() {
        return "QnaWriteReqDto{" +
                "qnaType=" + qnaType +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionContent='" + questionContent + '\'' +
                '}';
    }

}
