package com.my.d4.deadlock;

/**
 * Created by dumin on 5/12/17.
 */
public class Consumer {

    private static Consumer consumer;
    private Consumer(){}

    public void handle(int i){
        writeIntoFile(String.format("%04d - number was handled",i));

    }

    private void writeIntoFile(String format) {
        System.out.println(format);
    }

    public static Consumer getInstance() {

        synchronized(Consumer.class) {
            if(consumer == null){
                consumer = new Consumer();
            }
        }
        return consumer;
    }

}
