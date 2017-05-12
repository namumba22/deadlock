package com.my.d4.deadlock;

import java.util.Random;

/**
 * Created by dumin on 5/12/17.
 */
public class Utils {
    public static int getDelay(){
        int newDigit = new Random().nextInt(5-1+1);
//        System.out.println(String.format("new digit = %04d",newDigit));
        return newDigit;
    }

}
