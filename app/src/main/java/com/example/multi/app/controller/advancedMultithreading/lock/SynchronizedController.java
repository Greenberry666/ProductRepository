package com.example.multi.app.controller.advancedMultithreading.lock;


// synchronized 是 Java 内置的同步机制，用于确保同一时间只有一个线程可以执行同步代码块。
//适用于锁持有时间较长的场景，因为锁的获取和释放操作相对简单。
//适用于需要同步的代码块较大或复杂的情况，因为 synchronized 提供了较为简单的同步机制。
//适用于需要确保线程安全的复杂操作，因为 synchronized 提供了较为全面的同步保障。

public class SynchronizedController {
    //同步方法:
    //public synchronized void synchronizedMethod() {
    //    // 同步代码块
    //}

    //同步代码块:
    //public void method() {
    //    synchronized (this) { // 锁定当前对象
    //        // 同步代码块
    //    }
    //    synchronized (MyClass.class) { // 锁定类对象
    //        // 同步代码块
    //    }
    //    synchronized (new Object()) { // 锁定任意对象
    //        // 同步代码块
    //    }
    //}
    private int count = 0;

    // 同步方法
    public synchronized void increment() {
        count++;
    }

    public static void main(String[] args) {
        SynchronizedController example = new SynchronizedController();

        // 创建10个线程，每个线程调用increment方法1000次
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    example.increment();
                }
            }).start();
        }

        // 等待所有线程完成
        try {
            Thread.sleep(2000); // 简单等待，确保所有线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("最后的数: " + example.count);
    }
    }

