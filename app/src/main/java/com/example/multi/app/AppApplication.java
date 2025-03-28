package com.example.multi.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.example.multi")
@MapperScan(basePackages = {"com.example.multi.module.product.mapper"})
@MapperScan(basePackages = {"com.example.multi.module.category.mapper"})
@MapperScan(basePackages = {"com.example.multi.module.user.mapper"})

public class AppApplication {
    public static void main(String[] args){


        SpringApplication.run(AppApplication.class,args);
    }
}
