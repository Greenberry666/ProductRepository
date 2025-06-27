package com.example.multi.app.controller.advancedMultithreading.threadSynchronization;

import java.util.concurrent.Semaphore;

//用于控制对共享资源的访问，限制同时访问的线程数量。
//通过acquire()方法获取许可证，通过release()方法释放许可证。
//用于需要限制资源并发访问的场景，例如数据库连接池、线程池等。
//使用场景：限制对共享资源的并发访问/控制线程池中同时运行的线程数量。

public class SemaphoreController {
    public static void main(String[] args) {
        // 允许最多5个线程同时访问
        Semaphore semaphore = new Semaphore(5);

        // 启动10个线程
        for (int i = 0; i < 10; i++) {
            final int threadNumber = i + 1;
            new Thread(() -> {
                try {
                    // 获取许可证
                    semaphore.acquire();
                    System.out.println("线程 " + threadNumber + " 已获取许可证，开始访问资源。");

                    // 模拟线程工作
                    Thread.sleep((int) (Math.random() * 5000));

                    // 释放许可证
                    semaphore.release();
                    System.out.println("线程 " + threadNumber + " 已释放许可证，结束访问资源。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Thread-" + threadNumber).start();
        }
    }
}