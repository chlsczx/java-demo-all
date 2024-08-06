package com.czx.demo.springmvc.interceptor.config;

import com.czx.demo.springmvc.interceptor.controller.interceptor.ProjectInterceptor;
import com.czx.demo.springmvc.interceptor.controller.interceptor.ProjectInterceptor2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(SpringMvcConfig.class);

    @Autowired
    @Qualifier("projectInterceptor1")
    private ProjectInterceptor projectInterceptor;

    @Autowired
    @Qualifier("projectInterceptor2")
    private ProjectInterceptor2 projectInterceptor2;

    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     if (!registry.hasMappingForPattern("/sta/**")) {
    //         registry.addResourceHandler("/sta/**").addResourceLocations("file:D:\\my-dev\\demo-java\\java-demo-all\\staticc\\");
    //     }
    // }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(projectInterceptor).addPathPatterns("/user");
        registry.addInterceptor(projectInterceptor2).addPathPatterns("/user");
    }
}
