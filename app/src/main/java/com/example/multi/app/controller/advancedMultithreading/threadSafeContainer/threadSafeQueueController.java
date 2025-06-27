package com.example.multi.app.controller.advancedMultithreading.threadSafeContainer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class threadSafeQueueController {
    private static final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    //BlockingQueue是一个线程安全的队列接口，提供了阻塞操作，适用于生产者-消费者模式。
    //LinkedBlockingQueue 内部的锁机制确保了在多线程环境下对队列的读写操作是线程安全的。
    //使用场景:适用于需要在多线程环境下进行高效数据交换的场景，例如生产者-消费者模式、任务队列等。

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put(i);
                    //put() 方法将元素插入队列，如果队列已满，将阻塞直到队列有可用空间。
                    System.out.println("生产者放入: " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    int value = queue.take();
                    //take() 方法从队列中取出元素，如果队列为空，将阻塞直到队列中有数据可用。
                    //使用 queue.take() 方法从队列中取出数据，该方法会阻塞直到队列中有数据可用。
                    System.out.println("消费者取出: " + value);
                    if (value == 9) {
                        break; // 如果消费到9，退出循环
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
