package com.czx.demo.spring.mvc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @Autowired
    @Qualifier("aabb")
    String aabb;

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(
                    name = "name",
                    required = false,
                    defaultValue = "World")
            String name,
            Model model
            ) {
        model.addAttribute("name", name);
        model.addAttribute("aabb", aabb);
        return "greeting";
    }
}
