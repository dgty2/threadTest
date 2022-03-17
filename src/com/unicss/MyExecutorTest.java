package com.unicss;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class MyExecutorTest extends Thread {

    public static AtomicLong i = new AtomicLong(0);

    public void run() {
        try {
            System.out.println("i = " + i);
            //incrementAndGet返回的是新值（即加1后的值） 原子性
            //unsafe 直接操作内存native引用外部方法
            while (i.incrementAndGet() <= 1000000) {
                System.out.println(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService service = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 10; i++) {
            service.execute(new MyExecutorTest());
        }
        service.shutdown();
    }

}
