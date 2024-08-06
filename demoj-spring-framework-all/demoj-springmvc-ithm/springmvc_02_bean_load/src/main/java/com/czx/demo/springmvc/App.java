package com.czx.demo.springmvc;

import com.czx.demo.springmvc.config.SpringConfig;
import com.czx.demo.springmvc.controller.UserController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
//        ctx.register(SpringConfig.class);
//        ctx.refresh();
        System.out.println(ctx.getBean(UserController.class));
    }
}
