package com.example.multi.app.domain;

import com.example.multi.module.response.entity.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class EnhanceException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<String> handleException(Exception exception) {
        //log.error("系统繁忙,请稍后再试", exception);
        return Response.error(4004, "系统繁忙,请稍后再试");
    }
}
