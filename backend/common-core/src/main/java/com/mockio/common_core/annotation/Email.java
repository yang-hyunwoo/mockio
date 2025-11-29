package com.mockio.common_core.annotation;

import com.mockio.common_core.annotation.annotationValidator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 이메일 annotation
 * @Email(message= 메시지 , group= 단일 에러 리턴시 순서 )
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface Email {
    
    String message() default "{email.pattern}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
