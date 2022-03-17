package com.unicss;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCyclicBarrier {

    // 徒步需要的时间: Shenzhen, Guangzhou, Shaoguan, Changsha, Wuhan
    private static final int[] timeWalk = {5, 8, 15, 15, 10};
    // 自驾游
    private static final int[] timeSelf = {1, 3, 4, 4, 5};
    // 旅游大巴
    private static final int[] timeBus = {2, 4, 6, 6, 7};

    static String now() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date()) + ": ";
    }

    static class Tour implements Runnable {

        private final int[] times;
        private final CyclicBarrier barrier;
        private final String tourName;

        public Tour(CyclicBarrier barrier, String tourName, int[] times) {
            this.times = times;
            this.tourName = tourName;
            this.barrier = barrier;
        }

        public void run() {
            try {
                Thread.sleep(times[0] * 1000L);
                System.out.println(now() + tourName + " Reached Shenzhen");
                barrier.await();
                Thread.sleep(times[1] * 1000L);
                System.out.println(now() + tourName + " Reached Guangzhou");
                barrier.await();
                Thread.sleep(times[2] * 1000);
                System.out.println(now() + tourName + " Reached Shaoguan");
                barrier.await();
                Thread.sleep(times[3] * 1000);
                System.out.println(now() + tourName + " Reached Changsha");
                barrier.await();
                Thread.sleep(times[4] * 1000);
                System.out.println(now() + tourName + " Reached Wuhan");
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException ignored) {
            }
        }
    }

    public static void main(String[] args) {
        // 三个旅行团
        // TODO: 2022/3/16 所有线程会等待全部线程到达栅栏之后才会继续执行，并且最后到达的线程会完成 Runnable 的任务。
        CyclicBarrier barrier = new CyclicBarrier(3);
        ExecutorService exec = Executors.newFixedThreadPool(3);
        exec.submit(new Tour(barrier, "WalkTour", timeWalk));
        exec.submit(new Tour(barrier, "SelfTour", timeSelf));
        //当我们把下面的这段代码注释后，会发现，程序阻塞了，无法继续运行下去。
        exec.submit(new Tour(barrier, "BusTour", timeBus));
        exec.shutdown();
    }
} 