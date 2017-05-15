package com.my.d4.deadlock;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {

    public static void main(String... s) throws InterruptedException {


        List<Callable<Integer>> tasks = new ArrayList<>();

        tasks.add(() -> {
            Blocker.getInstance().go1();
            return null;
        });
        tasks.add(() -> {
            Blocker.getInstance().go2();
            return null;
        });

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.invokeAll(tasks)
                .stream()
                .map(future -> {
                    try {
//                        return future.get();
                        return future.get(2000, TimeUnit.MILLISECONDS);

                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                });



        executor.shutdown();
    }

}

