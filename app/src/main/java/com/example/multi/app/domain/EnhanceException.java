package com.example.multi.app.domain;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@ControllerAdvice
public class EnhanceException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception){
        //log.error("系统繁忙,请稍后再试",exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系统繁忙,请稍后再试");

    }
}
