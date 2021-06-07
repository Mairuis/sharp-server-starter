package com.mairuis.sharp.ringbuffer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author Mairuis
 * @since 2021/6/7
 */
public class CacheLineTests {

    @Test
    public void cacheLineTests() {
        int row = 1024 * 1024, column = 8, sum = 0;
        final long[][] array = new long[row][column];
        for (long[] longs : array) {
            Arrays.fill(longs, 1);
        }
        long time = System.currentTimeMillis();
        for (long[] longs : array) {
            for (long number : longs) {
                sum += number;
            }
        }
        System.out.println("cost:" + (System.currentTimeMillis() - time));

        sum = 0;
        time = System.currentTimeMillis();
        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < row; j++) {
                sum += array[j][i];
            }
        }
        System.out.println("cost:" + (System.currentTimeMillis() - time));
    }

}
