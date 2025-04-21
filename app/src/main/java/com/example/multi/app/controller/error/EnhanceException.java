package com.example.multi.app.controller.error;

import com.example.multi.module.utils.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class EnhanceException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<String> handleException(Exception exception) {
        //log.error("系统繁忙,请稍后再试", exception);
        return new Response(4004);
    }
}
