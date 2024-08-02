package com.czx.demo.spring.validation.ee;


import com.czx.demo.spring.validation.entity.Person;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import org.hibernate.validator.HibernateValidator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationUtil {
    private static Validator validator;
    private static Validator fastFailValidator;
    private static ExecutableValidator executableValidator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        fastFailValidator =
                Validation
                        .byProvider(HibernateValidator.class)
                        .configure()
                        .failFast(true)
                        .buildValidatorFactory()
                        .getValidator();

        executableValidator = validator.forExecutables();
    }

    public static List<String> valid(Person person) {
        Set<ConstraintViolation<Person>> set = validator.validate(person);
        List<String> collect
                = set.stream().map(v -> "属性：" + v.getPropertyPath()
                        + "，属性值：" + v.getInvalidValue()
                        + "，校验不通过的提示信息：" + v.getMessage())
                .collect(Collectors.toList());

        return collect;
    }

    public static List<String> validFastFail(Person person) {
        Set<ConstraintViolation<Person>> set = fastFailValidator.validate(person);

        List<String> collect
                = set.stream().map(v -> "属性：" + v.getPropertyPath()
                        + "，属性值：" + v.getInvalidValue()
                        + "，校验不通过的提示信息：" + v.getMessage())
                .collect(Collectors.toList());

        return collect;
    }

    public static <T> List<String> validNotBean(T object,
                                            Method method,
                                            Object[] parameterValues,
                                            Class<?>... groups) {
        Set<ConstraintViolation<T>> set = executableValidator.validateParameters(object, method, parameterValues, groups);
        List<String> collect
                = set.stream().map(v -> "属性：" + v.getPropertyPath()
                        + "，属性值：" + v.getInvalidValue()
                        + "，校验不通过的提示信息：" + v.getMessage())
                .collect(Collectors.toList());

        return collect;
    }
}
