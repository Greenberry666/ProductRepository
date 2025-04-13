package com.example.multi.module.response.entity;

import lombok.Data;

@Data
public class Response<T> {
    private int code; // 状态码
    private String msg; // 返回的消息
    private T data; // 返回数据，类型为T。T的类型不是固定的，而是由调用者指定的。


    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "ok", data);
    }

    public static <T> Response<T> success(int code, String msg, T data) {
        return new Response<>(code, msg, data);
    }

    public static <T> Response<T> error(int code, String msg) {
        return new Response<>(code, msg, null);
    }
}
