package com.czx.demo.springmvc.exception.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
        value = "com.czx.demo.springmvc.exception",
        excludeFilters = {
                @ComponentScan.Filter(
                        classes = {Controller.class}
                )
        }
)
public class SpringAppConfig {
}
