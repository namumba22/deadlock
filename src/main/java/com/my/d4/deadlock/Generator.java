package com.my.d4.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by dumin on 5/12/17.
 */
public class Generator {

    static Generator generator;

    int i = 0;

    ReadWriteLock lock = new ReentrantReadWriteLock();

    public int incAndGet() throws InterruptedException {
        lock.writeLock().lock();
        try {
            TimeUnit.MILLISECONDS.sleep(Utils.getDelay());
            return i++;
        } finally {
            lock.writeLock().unlock();
        }
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
