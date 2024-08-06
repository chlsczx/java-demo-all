package com.czx.demo.springboot.quartz.controller;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AnyController {

    @Autowired
    Scheduler scheduler;
}
