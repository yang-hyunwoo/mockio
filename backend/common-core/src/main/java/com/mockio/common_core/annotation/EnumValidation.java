package com.mockio.common_core.annotation;

import com.mockio.common_core.annotation.annotationValidator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * enum class 유효성 검사
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumValidation {
    Class<? extends Enum<?>> enumClass();
    String message() default "올바른 값이 아닙니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}

