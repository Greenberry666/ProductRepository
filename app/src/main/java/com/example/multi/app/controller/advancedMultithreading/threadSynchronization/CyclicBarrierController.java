package com.example.multi.app.controller.advancedMultithreading.threadSynchronization;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//CyclicBarrier用于一组线程互相等待，直到所有线程都到达一个公共的屏障点。
//每个线程在到达屏障点后调用await()方法等待，当所有线程都到达屏障点后，所有线程继续执行。
//通过CyclicBarrier可以实现多线程的同步执行，确保所有线程在某个点上同步进行。

public class CyclicBarrierController {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        // 开启线程1
        new Thread(() -> {
            System.out.println("线程1 第一部分逻辑开始.");
            try {
                // 第一部分逻辑
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程1 第一部分逻辑结束，等待其他线程.");
            try {
                // 在屏障点等待
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("线程1 第二部分逻辑开始.");
            try {
                // 第二部分逻辑
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程1 第二部分逻辑结束.");
        }, "Thread-1").start();

        // 开启线程2
        new Thread(() -> {
            System.out.println("线程2 第一部分逻辑开始.");
            try {
                // 第一部分逻辑
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2 第一部分逻辑结束，等待其他线程.");
            try {
                // 在屏障点等待
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("线程2 第二部分逻辑开始.");
            try {
                // 第二部分逻辑
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程2 第二部分逻辑结束.");
        }, "Thread-2").start();

        // 开启线程3
        new Thread(() -> {
            System.out.println("线程3 第一部分逻辑开始.");
            try {
                // 第一部分逻辑
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程3 第一部分逻辑结束，等待其他线程.");
            try {
                // 在屏障点等待
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("线程3 第二部分逻辑开始.");
            try {
                // 第二部分逻辑
                Thread.sleep((int) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程3 第二部分逻辑结束.");
        }, "Thread-3").start();
    }
}