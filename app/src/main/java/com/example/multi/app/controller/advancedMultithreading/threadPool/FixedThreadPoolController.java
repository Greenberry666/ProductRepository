package com.example.multi.app.controller.advancedMultithreading.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//特点:
//1>线程数量固定
//2>适用于任务数量较多且任务执行时间较长的场景
//3>线程池中的线程数量在创建时确定，不会动态变化


public class FixedThreadPoolController {
    public static void main(String[] args) {
        // 创建一个固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // 提交任务到线程池
        for (int i = 0; i < 10; i++) {
            int taskNumber = i + 1;
            executorService.submit(() -> {
                System.out.println("任务 " + taskNumber + " 正在执行，线程: " + Thread.currentThread().getName());
                try {
                    // 模拟任务执行时间
                    Thread.sleep((int) (Math.random() * 5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务 " + taskNumber + " 执行完成，线程: " + Thread.currentThread().getName());
            });
        }

        // 关闭线程池
        executorService.shutdown();
    }
}