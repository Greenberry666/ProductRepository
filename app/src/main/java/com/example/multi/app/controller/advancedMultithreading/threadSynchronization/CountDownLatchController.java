package com.example.multi.app.controller.advancedMultithreading.threadSynchronization;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;

//用于一个或多个线程等待多个线程完成任务。
//不可重用，一旦计数器减到0，CountDownLatch 就不能再次使用。
//主线程等待多个子线程完成任务。
//每个线程在完成任务后调用 countDown() 方法，计数器减1。

public class CountDownLatchController {
    @SneakyThrows
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        // 开启线程1
        new Thread(() -> {
            System.out.println("线程1 已开始工作.");
            try {
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程1 已结束工作.");
            countDownLatch.countDown();
        }).start();

        // 开启线程2
        new Thread(() -> {
            System.out.println("线程2 已开始工作.");
            try {
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2 已结束工作.");
            countDownLatch.countDown();
        }).start();

        // 开启线程3
        new Thread(() -> {
            System.out.println("线程3 已开始工作.");
            try {
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程3 已结束工作.");
            countDownLatch.countDown();
        }).start();

        // 主线程等待所有线程完成
        System.out.println("主线程正在等待所有线程完成。");
        countDownLatch.await();
        System.out.println("所有线程已完成,主线程继续。");
    }
}