package com.czx.demo.springmvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
        value = "com.czx.demo.springmvc"
        ,
        excludeFilters = {
                @ComponentScan.Filter(Controller.class)
        }
)
public class SpringConfig {
}
