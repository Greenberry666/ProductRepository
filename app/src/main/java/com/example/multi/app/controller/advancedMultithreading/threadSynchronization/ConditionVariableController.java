package com.example.multi.app.controller.advancedMultithreading.threadSynchronization;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionVariableController {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static int sharedData = 0;
    private static boolean done = false; // 添加一个标志，表示生产者是否完成生产

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                lock.lock();
                try {
                    sharedData = i;
                    System.out.println("生产者生产数据: " + sharedData);
                    condition.signal(); // 通知消费者
                } finally {
                    lock.unlock();
                }
            }
            lock.lock();
            try {
                done = true; // 设置完成标志
                condition.signal(); // 通知消费者生产者已经完成
            } finally {
                lock.unlock();
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    while (sharedData == 0 && !done) { // 等待数据或生产者完成
                        condition.await();
                    }
                    if (done && sharedData == 0) { // 如果生产者完成且没有数据可消费，退出循环
                        break;
                    }
                    System.out.println("消费者消费数据: " + sharedData);
                    sharedData = 0; // 消费数据
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join(); // 等待消费者线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}