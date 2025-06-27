package com.example.multi.app.controller.advancedMultithreading.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//特点:
//1>线程数量不定，根据需要动态创建线程
//2>适用于任务数量较多且任务执行时间较短的场景
//3>线程池会根据需要创建新线程，但会复用已有的空闲线程，减少线程创建和销毁的开销

public class CachedThreadPoolController {
    public static void main(String[] args) {
        // 创建一个可缓存的线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

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