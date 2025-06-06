package com.example.multi.module.utils;

import lombok.Data;


@Data
public class Response<T> {
    private final ResponseStatus status = new ResponseStatus();
    private final T result;


    public Response(int status) {
        this.status.setCode(status);
        this.status.setMsg(ResponseCode.getMsg(status));
        this.result = null;
    }


    public Response(int status, T result) {
        this.status.setCode(status);
        this.status.setMsg(ResponseCode.getMsg(status));
        this.result = result;
    }
}


