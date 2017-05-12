package com.my.d4.deadlock;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Producer {

    public final static int THREAD_QUATN = 1000;
    public final static int FIXED_THREADS = 100;

    private static class MyCallable implements Callable<Integer> {
        final int timeout;

        MyCallable(int timeout) {
            this.timeout = timeout;
        }

        @Override
        public Integer call() throws Exception {

            AtomicInteger i;
            try {
                TimeUnit.MICROSECONDS.sleep(timeout);
                Generator.getInstance().incAndGet();      // TODO returns less then THREAD_QUATN
//                Generator.getInstance().incAndGetSync();   // TODO returns THREAD_QUATN
//                Consumer.getInstance().handle(Generator.getInstance().incAndGet());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

//    static Callable<Integer> task = () -> {
//        try {
//            TimeUnit.MICROSECONDS.sleep(Utils.getDelay());
//            Consumer.getInstance().handle(Generator.getInstance().incAndGet());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    };
//
    static List<Callable<Integer>> tasks = new ArrayList<>();

    public static void main(String... s) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(FIXED_THREADS);

        IntStream.range(0, THREAD_QUATN).forEach(i -> tasks.add(new MyCallable(Utils.getDelay())));

        executor.invokeAll(tasks)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                });
//                .forEach(System.out::println);

//        IntStream.range(0, 10000)
//                .forEach(i -> executor.submit(task));
        executor.shutdown();
//        executor.awaitTermination(10L, TimeUnit.SECONDS);

//        TimeUnit.SECONDS.sleep(10);

        System.out.println(Generator.getInstance().getI());

    }
}
