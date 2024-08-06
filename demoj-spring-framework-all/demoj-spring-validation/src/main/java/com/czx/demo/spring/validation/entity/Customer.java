package com.czx.demo.spring.validation.entity;

import com.czx.demo.spring.validation.anno.PersonStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Customer {
    public interface NonBlank {

    }

    public interface Default {

    }
    @NotBlank(message = "不得为空")
//    @Size(min = 1, max = 50)
    private String firstName;

    @PersonStatus()
    private int status;

    @NotNull
    @Size(min = 1, max = 50)
    private String surname;

    @NotNull
    @Valid
    private Address address;

    public Customer() {
    }

    public Customer(String firstName, int status, String surname, Address address) {
        this.firstName = firstName;
        this.status = status;
        this.surname = surname;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", status=" + status +
                ", surname='" + surname + '\'' +
                ", address=" + address +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(@PersonStatus int status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
