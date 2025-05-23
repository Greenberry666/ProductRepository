package com.example.multi.console;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.multi")
@MapperScan(basePackages = {"com.example.multi.module.product.mapper"})
@MapperScan(basePackages = {"com.example.multi.module.category.mapper"})
@MapperScan(basePackages = {"com.example.multi.module.user.mapper"})
@MapperScan(basePackages = {"com.example.multi.module.tag.mapper"})
@MapperScan(basePackages = {"com.example.multi.module.productTag.mapper"})
public class ConsoleApplication {
    public static void main(String[] args){

        SpringApplication.run(ConsoleApplication.class,args);
    }
}

