package com.my.d4.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dumin on 5/12/17.
 */
public class Generator {

    static Generator generator;

    int i = 0;

//    private Generator() {
//    }

    public void incAndGet() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
//        try {
        TimeUnit.MILLISECONDS.sleep(Utils.getDelay());
        i += 1;
        lock.unlock();
//        } finally {
//        }
    }

    public synchronized void incAndGetSync() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(Utils.getDelay());
        i += 1;
    }


    public static Generator getInstance() {

        synchronized (Generator.class) {
            if (generator == null) {
                generator = new Generator();
            }
        }
        return generator;
    }

    public int getI() {
        return i;
    }


}
