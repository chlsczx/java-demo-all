package com.czx.demo.spring.validation.anno;


import com.czx.demo.spring.validation.conf.validator.PersonStatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * PersonStatus must be 1001, 1002 or 1003.
 * Accepts {@code Integer}.
 */
@Documented
@Constraint(validatedBy = {PersonStatusValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface PersonStatus {

    String message() default "{jakarta.validation.constraints.PersonStatus.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
