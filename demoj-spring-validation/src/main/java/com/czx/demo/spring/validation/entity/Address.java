package com.czx.demo.spring.validation.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Address {
    @NotNull
    @Size(min = 2, max = 50, message = "[street] string length must >=2 and <= 50")
    private String street;

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                '}';
    }

    public @NotNull @Size(min = 2, max = 50) String getStreet() {
        return street;
    }

    public void setStreet(@NotNull @Size(min = 2, max = 50) String street) {
        this.street = street;
    }
}
