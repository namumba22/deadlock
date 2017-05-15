package com.my.d4.deadlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dumin on 5/15/17.
 */
public class Blocker {
    private static final Logger LOGGER = LoggerFactory.getLogger(Blocker.class);

    private Blocker() {
    }

    static Blocker blocker;


    public static Blocker getInstance() {

        synchronized (Blocker.class) {
            if (blocker == null) {
                blocker = new Blocker();
            }
        }
        return blocker;
    }


    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    void go2() throws InterruptedException {

        lock2.lock();
        LOGGER.info("go2 get asleep");
        TimeUnit.MILLISECONDS.sleep(100);
        LOGGER.info("go2 awake");
        lock1.lock();

        try {
        } finally {
            lock1.unlock();
            lock2.unlock();
        }

    }

    void go1() throws InterruptedException {

        lock1.lock();
        LOGGER.info("go1 get asleep");
        TimeUnit.MILLISECONDS.sleep(100);
        LOGGER.info("go1 awake");
        lock2.lock();

        try {
        } finally {
            lock2.unlock();
            lock1.unlock();
        }
    }
}



