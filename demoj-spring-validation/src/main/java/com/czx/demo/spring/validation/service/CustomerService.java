package com.czx.demo.spring.validation.service;

import com.czx.demo.spring.validation.ee.ValidationUtil;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Method;
import java.util.List;

public class CustomerService {

    public String getByName(@NotNull String name) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        String methodName = stackTraceElement.getMethodName();
        System.out.println(methodName);
        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod(methodName, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> list = ValidationUtil.validNotBean(this, method, new Object[]{name});
        System.out.println(list);
        return "ok";
    }

    public static void main(String[] args) {
        CustomerService service = new CustomerService();
        String byName = service.getByName(null);
    }
}
