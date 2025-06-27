package com.example.multi.app.controller.advancedMultithreading.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//特点:
//1>线程数量固定为1。适用于任务需要按顺序执行的场景
//2>适用于任务需要按顺序执行的场景
//3>所有任务都在同一个线程中按提交顺序执行

public class SingleThreadExecutorController {
    public static void main(String[] args) {
        // 创建一个单线程的线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

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