package com.czx.demo.springmvc.interceptor.controller.interceptor;

import com.czx.demo.springmvc.interceptor.model.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Component("projectInterceptor1")
public class ProjectInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ProjectInterceptor.class);
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("> pre-handle");
        String s = objectMapper.writeValueAsString(new Result("nonono not good", 404, List.of("1", "2")));
        response.getWriter().write(s);
        response.addHeader("content-type", "application/json;charset=utf-8");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("> post-handle");
        return;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("> after-completion");
    }

    public ProjectInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
