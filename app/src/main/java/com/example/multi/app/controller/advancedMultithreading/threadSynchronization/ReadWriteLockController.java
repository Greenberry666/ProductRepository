package com.example.multi.app.controller.advancedMultithreading.threadSynchronization;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockController {
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    //ReentrantReadWriteLock:是一个可重入的读写锁，允许多个线程同时读取共享资源，但写操作是互斥的。

    //读锁允许多个线程同时读取共享资源，但写锁是互斥的，确保在写操作时不会被其他线程中断。
    private static int sharedData = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            lock.readLock().lock();
            //读线程通过lock.readLock().lock()获取读锁，读取sharedData后通过lock.readLock().unlock()释放读锁。
            try {
                System.out.println("读取数据 " + sharedData);
            } finally {
                lock.readLock().unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            lock.readLock().lock();
            try {
                System.out.println("读取数据: " + sharedData);
            } finally {
                lock.readLock().unlock();
            }
        });

        Thread t3 = new Thread(() -> {
            lock.writeLock().lock();
            try {
                sharedData++;
                System.out.println("记录数据: " + sharedData);
            } finally {
                lock.writeLock().unlock();
                //写线程通过lock.writeLock().lock()获取写锁，修改sharedData后通过lock.writeLock().unlock()释放写锁。
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
