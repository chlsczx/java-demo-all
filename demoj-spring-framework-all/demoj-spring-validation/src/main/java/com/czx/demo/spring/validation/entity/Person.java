package com.czx.demo.spring.validation.entity;

import com.czx.demo.spring.validation.anno.PersonStatus;
import jakarta.validation.constraints.NotBlank;

public class Person {

    @NotBlank
    private String name;

    private int age;

    @PersonStatus(message = "必须是 1001 1002 1003")
    private int status;

    // the usual getters and setters...


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}