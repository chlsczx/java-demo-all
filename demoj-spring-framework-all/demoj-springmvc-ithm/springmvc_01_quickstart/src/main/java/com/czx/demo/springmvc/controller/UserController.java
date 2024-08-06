package com.czx.demo.springmvc.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @PostMapping("/save")
    @ResponseBody
    public String save(@RequestBody List<String> name) {
//        String name = map.get("name");
        System.out.println("user " + name + " save");

        return "{ 'info': 'springmvc', 'name': '"+name+"' }";
    }

    @PostMapping("/date")
    @ResponseBody
    public String date(@RequestParam MultiValueMap<String, String> params,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        System.out.println("user " + params + " date");
        System.out.println(date);
        return params.getFirst("name");
    }
}
