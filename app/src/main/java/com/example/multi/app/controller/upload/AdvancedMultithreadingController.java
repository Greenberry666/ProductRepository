package com.example.multi.app.controller.upload;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
public class AdvancedMultithreadingController {
    //互斥锁Mutex
    private static final Lock lock = new ReentrantLock();
    //ReentrantLock用于保护对sharedData的访问，保证了线程安全
    private static int sharedData = 0;
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                lock.lock();
                try {
                    sharedData++;
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                lock.lock();
                try {
                    sharedData++;
                } finally {
                    lock.unlock();
                }
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

        System.out.println("sharedData: " + sharedData);
    }


    private final List<Integer> list;

    public AdvancedMultithreadingController() {
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
