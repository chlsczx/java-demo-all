package com.czx.demo.spring.validation.conf.validator;

import com.czx.demo.spring.validation.anno.PersonStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class PersonStatusValidator implements ConstraintValidator<PersonStatus, Integer> {
    @Override
    public void initialize(PersonStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return Arrays.asList(1001, 1002, 1003).contains(value);
    }
}
