package com.czx.demo.springmvc.interceptor.model.dto;

import java.io.Serial;
import java.io.Serializable;

public class Result implements Serializable {

    static final long serialVersionUID = 1L;

    private String msg;
    private Integer code;
    private Object data;

    public Result(String msg, Integer code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
