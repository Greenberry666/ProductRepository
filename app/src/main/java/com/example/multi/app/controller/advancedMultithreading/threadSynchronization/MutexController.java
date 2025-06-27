package com.example.multi.app.controller.advancedMultithreading.threadSynchronization;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexController {
    private static final Lock lock = new ReentrantLock();
    //使用 finally 块确保即使在修改 sharedData 时发生异常，锁也会被正确释放，避免死锁问题。

    //ReentrantLock:是一个可重入的互斥锁，提供了比synchronized更灵活的锁操作。
    //它允许线程在进入同步块之前显式地获取锁，并在退出同步块时显式地释放锁。

    private static int sharedData = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                lock.lock();
                //使用lock.lock()获取锁，确保在修改sharedData时不会被其他线程中断
                try {
                    //在try块中执行sharedData++操作，确保在发生异常时能够正确释放锁。
                    sharedData++;
                } finally {
                    //在finally块中调用lock.unlock()释放锁，保证锁的释放不会因异常而遗漏。
                    lock.unlock();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
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

        System.out.println("共享数据: " + sharedData);
    }
}
