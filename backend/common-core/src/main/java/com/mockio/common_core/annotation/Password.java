package com.mockio.common_core.annotation;

import com.mockio.common_core.annotation.annotationValidator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 비밀번호 annotation
 *  Password(message=메시지 , group= 단일 에러 리턴시 순서)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "{password.pattern}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
