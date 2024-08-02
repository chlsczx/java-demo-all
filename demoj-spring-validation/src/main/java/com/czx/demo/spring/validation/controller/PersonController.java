package com.czx.demo.spring.validation.controller;

import com.czx.demo.spring.validation.entity.Customer;
import com.czx.demo.spring.validation.entity.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

@Controller
public class PersonController {


    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final Validator personValidator;

    public PersonController(@Qualifier("personValidator") Validator personValidator) {
        this.personValidator = personValidator;
    }

    @PostMapping("/p")
    @ResponseBody
    public JsonNode addPerson(@RequestBody Person person) {
        Errors errors = personValidator.validateObject(person);

        ObjectMapper objectMapper = new ObjectMapper();
        if (errors.hasErrors()) {
            JsonNode jsonString = objectMapper.valueToTree(errors.toString());
            log.info(String.valueOf(jsonString));
            return jsonString;
        }

        return objectMapper.valueToTree("success");
    }

    @PostMapping("/c")
    @ResponseBody
    public String addCustomer(@Valid @RequestBody Customer customer) {
        System.out.println(customer);
//        if (bindingResult.hasErrors()) {
//
//            List<ObjectError> allErrors = bindingResult.getAllErrors();
//            for (ObjectError objectError : allErrors) {
//                System.out.println(objectError.getObjectName() + " : " + objectError.getDefaultMessage());
//            }
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            int i = 0;
//            for (FieldError fieldError : fieldErrors) {
//                System.out.println(i++);
//                System.out.println(fieldError.getField()
//                        + " : " + fieldError.getDefaultMessage()
//                        + " : " + fieldError.getRejectedValue());
//            }
//            return "bad request";
//        }
        return "success";
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public String handleException(BindException e) {
        List<FieldError> allErrors = e.getFieldErrors();
        HashMap<String, Object> map = new HashMap<>();
        for(FieldError error : allErrors) {
            map.put(error.getField(), error.getDefaultMessage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String r = objectMapper.valueToTree(map).toString();
        System.out.println(r);
        return r;
    }

}
