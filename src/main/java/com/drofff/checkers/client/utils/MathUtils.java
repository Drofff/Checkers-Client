package com.drofff.checkers.client.utils;

import java.util.stream.IntStream;

public class MathUtils {

    private MathUtils() {}

    public static int avg(int ... nums) {
        int sum = IntStream.of(nums).sum();
        return sum / nums.length;
    }

    public static int diff(int num0, int num1) {
        return Math.abs(num0 - num1);
    }

}