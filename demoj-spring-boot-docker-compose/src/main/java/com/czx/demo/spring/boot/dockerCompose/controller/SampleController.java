package com.czx.demo.spring.boot.dockerCompose.controller;

import com.czx.demo.spring.boot.dockerCompose.entity.User;
import com.czx.demo.spring.boot.dockerCompose.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class SampleController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/demo")
    public List<User> getUser() {
        List<User> result = userMapper.selectAll();
        log.info("result{}", result);
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @GetMapping("/demo/add")
    public String addUser(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        User user = new User(name, age);
        return String.valueOf(userMapper.insert(user));
    }

    @GetMapping("/demo/deleteAll")
    public String deleteAll() {
        return String.valueOf(userMapper.deleteAll());
    }
}
