package com.czx.demo.spring.servlet.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class MyUserHandler {

    public ServerResponse getUser(ServerRequest request) {
        /**/
        return ServerResponse.ok().body(request.pathVariable("user") + " get");
    }

    public ServerResponse getUserCustomers(ServerRequest request) {
        /**/
        return ServerResponse.ok().body(request.pathVariable("user") + " get customers");
    }

    public ServerResponse deleteUser(ServerRequest request) {
        /**/
        return ServerResponse.ok().body(request.pathVariable("user") + " delete");
    }

}