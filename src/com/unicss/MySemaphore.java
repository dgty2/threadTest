package com.unicss;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MySemaphore extends Thread implements Callable<String> {

    Semaphore position;
    private int id;

    public MySemaphore(int i, Semaphore s) {
        this.id = i;
        this.position = s;
    }

    public void run() {

        try {
            if (position.availablePermits() > 0) {
                System.out.println("可用信号= " + position.availablePermits());
                System.out.println("顾客[" + this.id + "]进入厕所，有空位");
            } else {
                System.out.println("顾客[" + this.id + "]进入厕所，没空位，排队");
            }
            //从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
            //进入不一定能抢到 类似红包
            position.acquire();
            System.out.println("顾客[" + this.id + "]获得坑位");
            long sellp = (int) (Math.random() * 1000);
            Thread.sleep(sellp);
            System.out.println("顾客[" + this.id + "]使用间隔" + sellp);
            System.out.println("顾客[" + this.id + "]使用完毕");
            position.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService list = Executors.newCachedThreadPool();
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(
                list);
        Semaphore position = new Semaphore(2);
        for (int i = 0; i < 10; i++) {
            // TODO: 2022/3/16 get 阻塞执行
      /*  try {
                list.submit(new MySemaphore(i + 1, position)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/

            completionService.submit(new MySemaphore(i + 1, position));
            /*s = completionService.take().get();
            System.out.println("s = " + s);*/

            //   list.submit(new MySemaphore(i + 1, position));
        }
        list.shutdown();
        //使等待进入acquire()方法的线程，不允许被中断。
        position.acquireUninterruptibly(2);
        System.out.println("使用完毕，需要清扫了");
        position.release(2);
    }

    @Override
    public String call() throws Exception {
        try {
            if (position.availablePermits() > 0) {
                System.out.println("可用信号= " + position.availablePermits());
                System.out.println("顾客[" + this.id + "]进入厕所，有空位");
            } else {
                System.out.println("顾客[" + this.id + "]进入厕所，没空位，排队");
            }
            //从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
            //进入不一定能抢到 类似红包
            position.acquire();
            System.out.println("顾客[" + this.id + "]获得坑位");
            long sellp = (int) (Math.random() * 1000);
            Thread.sleep(sellp);
            System.out.println("顾客[" + this.id + "]使用间隔" + sellp);
            System.out.println("顾客[" + this.id + "]使用完毕");
            position.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "222";
    }

}