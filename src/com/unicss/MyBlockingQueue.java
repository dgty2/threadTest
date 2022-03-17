package com.unicss;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class MyBlockingQueue extends Thread {

    public static BlockingQueue<String> queue = new LinkedBlockingQueue<String>(3);
    private int index;

    public MyBlockingQueue(int i) {
        this.index = i;
    }

    public void run() {
        try {
            queue.put(String.valueOf(this.index));
            System.out.println("{" + this.index + "} in queue!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService service = Executors.newCachedThreadPool();
        /**
         * Future模式是多线程设计常用的一种设计模式。Future模式可以理解成：我有一个任务，提交给了Future，Future替我完成这个任务。期间我自己可以去做任何想做的事情。一段时间之后，我就便可以从Future那儿取出结果。
         * Future提供了三种功能：
         * 判断任务是否完成
         * 能够中断任务
         * 能够获取任务执行的结果
         * 向线程池中提交任务的submit方法不是阻塞方法，而Future.get方法是一个阻塞方法，当submit提交多个任务时，只有所有任务都完成后，才能使用get按照任务的提交顺序得到返回结果，所以一般需要使用future.isDone先判断任务是否全部执行完成，完成后再使用future.get得到结果。（也可以用get (long timeout, TimeUnit unit)方法可以设置超时时间，防止无限时间的等待）
         * 三段式的编程：1.启动多线程任务2.处理其他事3.收集多线程任务结果，Future虽然可以实现获取异步执行结果的需求，但是它没有提供通知的机制，要么使用阻塞，在future.get()的地方等待future返回的结果，这时又变成同步操作；要么使用isDone()轮询地判断Future是否完成，这样会耗费CPU的资源。
         * 解决方法：CompletionService和CompletableFuture（按照任务完成的先后顺序获取任务的结果）

         */
        for (int i = 0; i < 10; i++) {
            service.submit(new MyBlockingQueue(i));
        }
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep((int) (Math.random() * 1000));
                    System.out.println("=======" + MyBlockingQueue.queue.size());
                    if (MyBlockingQueue.queue.isEmpty()) {
                        break;
                    }
                    String str = MyBlockingQueue.queue.take();
                    System.out.println(str + " has take!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        service.submit(thread);
     /*
      Future<?> submit =
     boolean done = submit.isDone();
        if (done) {
            try {
                System.out.println("submit = " + submit.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }*/
        service.shutdown();
    }
}