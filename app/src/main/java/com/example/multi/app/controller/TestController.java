package com.example.multi.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    private final List<Integer> list;

    public TestController() {
        list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(1);
        }
    }
    @RequestMapping("/test/stringbuilder")
    public Integer stringbuilder(){
        StringBuilder builder = new StringBuilder();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                builder.append(list.get(i));
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 5000; i < 10000; i++) {
                builder.append(list.get(i));
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return builder.length();
    }

    @RequestMapping("test/stringbuffer")
    public Integer buffer(){
        StringBuffer buffer = new StringBuffer();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                buffer.append(list.get(i));
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 5000; i < 10000; i++) {
                buffer.append(list.get(i));
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.length();
    }


}
