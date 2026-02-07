package com.mockio.common_spring.annotation.annotationValidator;

import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.CustomApiFieldException;
import com.mockio.common_core.exception.CustomApiFieldListException;
import com.mockio.common_core.exception.ValidationErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mockio.common_core.constant.CommonErrorEnum.*;
import static org.springframework.http.HttpStatus.*;


/**
 유효성 검사 순서 검증 validator
 */
@Component
@RequiredArgsConstructor
public class SequentialValidator {

    private final Validator validator;

    /**
     * dto 유효성 검사 순차적으로 그룹별로 수행하여
     * 가장 먼저 실패하는 검증 하나만 CustomApiFieldException 예외로 던짐
     * @param dto
     * @param groupOrder
     * @param <T>
     */
    public <T> void validate(T dto, Class<?>[] groupOrder) {
        for (Class<?> group : groupOrder) {
            Set<ConstraintViolation<T>> violations = validator.validate(dto, group);
            if (!violations.isEmpty()) {
                ConstraintViolation<T> v = violations.iterator().next();
                String field = v.getPropertyPath().toString();
                String annotationName = v.getConstraintDescriptor().getAnnotation()
                        .annotationType().getSimpleName();

                // 어노테이션 + 필드명 기반으로 에러코드 동적 결정
                CommonErrorEnum errorEnum = mapErrorEnum(annotationName);
                throw new CustomApiFieldException(BAD_REQUEST.value(),v.getMessage(), errorEnum,field);
            }
        }
    }

    /**
     * dto 유효성 검사 순차적으로 그룹별로 수행하여
     * 모든 검증 에러  CustomApiFieldListException 예외로 던짐
     */
    public <T> void validateAll(T dto, Class<?>[] groupOrder) {
        List<ValidationErrorResponse> errorList = new ArrayList<>();
        for (Class<?> group : groupOrder) {
            Set<ConstraintViolation<T>> violations = validator.validate(dto, group);
            for (ConstraintViolation<T> v : violations) {
                String field = v.getPropertyPath().toString();
                errorList.add(new ValidationErrorResponse(field, v.getMessage()));
            }
        }
        if (!errorList.isEmpty()) {
            throw new CustomApiFieldListException(BAD_REQUEST, "유효성 검사 실패", ERR_002, errorList);
        }
    }

    private CommonErrorEnum mapErrorEnum(String annotation) {
        return switch (annotation) {
            case "NotBlank" -> ERR_001;
            default -> ERR_002;
        };
    }

}