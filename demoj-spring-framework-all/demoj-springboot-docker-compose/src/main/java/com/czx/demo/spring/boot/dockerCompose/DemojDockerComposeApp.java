package com.czx.demo.spring.boot.dockerCompose;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.czx.demo.spring.boot.dockerCompose.**.mapper")
public class DemojDockerComposeApp {
    public static void main(String[] args) {
        SpringApplication.run(DemojDockerComposeApp.class, args);
    }
}