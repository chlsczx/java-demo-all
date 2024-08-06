package com.czx.demo.springmvc.interceptor.controller.interceptor;

import com.czx.demo.springmvc.interceptor.model.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Component("projectInterceptor2")
public class ProjectInterceptor2 implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ProjectInterceptor2.class);
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("> pre-handle 222");
        String s = objectMapper.writeValueAsString(new Result("nanana", 404, List.of("1", "2")));
        response.getWriter().write(s);
        // response.addHeader("content-type", "application/json;charset=utf-8");
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

    public ProjectInterceptor2(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
