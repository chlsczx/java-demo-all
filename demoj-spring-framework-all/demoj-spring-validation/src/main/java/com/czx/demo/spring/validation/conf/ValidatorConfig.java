package com.czx.demo.spring.validation.conf;

import com.czx.demo.spring.validation.validator.PersonValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {
    @Bean(name = "personValidator")
    public PersonValidator personValidator() {
        return new PersonValidator();
    }
}
