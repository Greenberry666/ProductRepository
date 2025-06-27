package com.example.multi.app.controller.advancedMultithreading.lock;

import java.util.concurrent.atomic.AtomicInteger;

//CAS（Compare-And-Swap）是一种无锁的同步机制，通过比较和交换操作来实现线程安全。
//适用于锁持有时间较短的场景，因为 CAS 操作是非阻塞式的，减少了线程上下文切换的开销。
//适用于需要高性能的场景，尤其是在高并发环境下，CAS 可以显著提高性能。
//适用于简单的原子操作，如计数器、状态标志等，因为 CAS 操作需要明确的预期值和新值。

public class CASController {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet(); // 使用CAS操作原子地增加count
    }

    public static void main(String[] args) {
        CASController example = new CASController();

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

        System.out.println("最后的数: " + example.count.get());
    }
}