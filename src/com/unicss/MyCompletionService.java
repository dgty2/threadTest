package com.unicss;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyCompletionService implements Callable<String> {

    private int id;

    public MyCompletionService(int i) {
        this.id = i;
    }

    public static void main(String[] args) throws Exception {
        //  int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newCachedThreadPool();
        // TODO: 2022/3/16 异步执行直接获取结果
        CompletionService<String> completion = new ExecutorCompletionService<>(service);

        for (int i = 0; i < 10; i++) {
            /**
             * 0 start
             * 0 end
             * s = 0:746
             * 1 start
             * 1 end
             * s = 1:860
             按序执行 sumit回调get
             */


            //    TODO: 2022/3/16 CompletionService中submit后 get方法阻塞顺序执行
            completion.submit(new MyCompletionService(i));

         /*   String s =
                    System.out.println("s = " + s);*/
        }
        for (int i = 0; i < 10; i++) {
            // TODO: 2022/3/16 CompletionService中 take直接拿future结果 get非阻塞
            System.out.println(completion.take().get());
        }
        service.shutdown();
    }

    @Override
    public String call() throws Exception {
        Integer time = (int) (Math.random() * 1000);
        try {
            System.out.println(this.id + " start");
            Thread.sleep(time);
            System.out.println(this.id + " end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.id + ":" + time;
    }
}