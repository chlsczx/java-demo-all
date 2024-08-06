package com.czx.demo.spring.validation.ee;

import com.czx.demo.spring.validation.entity.Person;

import java.util.List;

public class ValidationTest {

    public static void main(String[] args) {
        Person person = new Person();
        person.setName("");


        {
            List<String> list = ValidationUtil.valid(person);
            System.out.println(list);
        }

        {
            List<String> list = ValidationUtil.validFastFail(person);
            System.out.println(list);
        }
    }
}
