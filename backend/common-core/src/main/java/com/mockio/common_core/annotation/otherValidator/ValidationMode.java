package com.mockio.common_core.annotation.otherValidator;


import com.mockio.common_core.constant.ValidationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationMode {
    ValidationType value() default ValidationType.SINGLE;
}

