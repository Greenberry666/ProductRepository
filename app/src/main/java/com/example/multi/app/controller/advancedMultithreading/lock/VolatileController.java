package com.example.multi.app.controller.advancedMultithreading.lock;


//volatile 是 Java 提供的一种轻量级的同步机制，用于确保变量的内存可见性

public class VolatileController {
    private volatile boolean running = true;

    public void run() {
        while (running) {
            // 模拟线程工作
            System.out.println("线程正在运行…");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("线程已停止");
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) {
        VolatileController example = new VolatileController();

        Thread t1 = new Thread(() -> example.run());
        t1.start();

        // 主线程等待一段时间后停止线程
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        example.stop();
    }
}