package com.example.multi.app.controller.advancedMultithreading.threadSafeContainer;

import java.util.concurrent.ConcurrentHashMap;

public class threadSafeHashTableController {
    private static final ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();
    //ConcurrentHashMap 是一个线程安全的哈希表实现，允许多个线程同时进行读写操作。
    //ConcurrentHashMap 内部的锁机制确保了在多线程环境下对哈希表的读写操作是线程安全的
    //通过分段锁机制（Segmented Locking）或 CAS（Compare-And-Swap）操作来实现线程安全，避免了使用单一锁导致的性能瓶颈。

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            map.put(1, "one");
            System.out.println("放入线程1: " + map.get(1));
        });

        Thread t2 = new Thread(() -> {
            map.put(2, "two");
            System.out.println("放入线程2: " + map.get(2));
        });

        Thread t3 = new Thread(() -> {
            String value = map.get(1);
            System.out.println("得到线程3: " + value);
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
