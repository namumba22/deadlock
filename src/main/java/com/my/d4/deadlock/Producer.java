package com.my.d4.deadlock;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Producer {

    public final static int THREAD_QUATN = 1000;
    public final static int FIXED_THREADS = 900;

    private static class Handler implements Callable<Integer> {
        final int timeout;

        Handler(int timeout) {
            this.timeout = timeout;
        }

        @Override
        public Integer call() throws Exception {
            try {
                TimeUnit.MICROSECONDS.sleep(timeout);
                Integer i = Generator.getInstance().incAndGet();
                Consumer.getInstance().handle(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static {
        Thread printDeamon = new Thread(){
            @Override
            public void run(){
                while (!Thread.currentThread().isInterrupted()) {
                    Consumer.getInstance().writeIntoFile();

                    try {
                        TimeUnit.MICROSECONDS.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        printDeamon.setDaemon(true);
        printDeamon.start();
    }

    static List<Callable<Integer>> tasks = new ArrayList<>();

    public static void main(String... s) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(FIXED_THREADS);

        IntStream.range(0, THREAD_QUATN).forEach(i -> tasks.add(new Handler(Utils.getDelay())));

        executor.invokeAll(tasks)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                });

        executor.shutdown();

        System.out.println(Generator.getInstance().getI());


    }
}
