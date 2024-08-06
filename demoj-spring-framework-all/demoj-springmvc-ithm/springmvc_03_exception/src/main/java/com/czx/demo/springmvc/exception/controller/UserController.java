package com.czx.demo.springmvc.exception.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Controller
public class UserController {
    @RequestMapping("/test")
    @ResponseBody
    public String test() throws Exception {
        throw new Exception("aaa");
        // return "hello";
    }
}
