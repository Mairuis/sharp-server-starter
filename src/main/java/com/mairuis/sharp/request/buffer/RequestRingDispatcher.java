package com.mairuis.sharp.request.buffer;

import com.mairuis.sharp.request.Request;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于环形数组的请求缓冲池
 * 支持多种饱和策略
 *
 * @author Mairuis
 * @since 2021/6/3
 */
public class RequestRingDispatcher implements RequestDispatcher {

    private static final int MAXIMUM_CAPACITY = 8096;

    private final Request[] elements;

    private final AtomicInteger sequenceId = new AtomicInteger(0);

    public RequestRingDispatcher(int capacity) {
        this.elements = new Request[ringSizeOf(capacity)];
    }

    private int nextId() {
        return Math.abs(sequenceId.getAndIncrement());
    }

    @Override
    public void publish(Request request) {

    }

    private static int ringSizeOf(int capacity) {
        int c = capacity - 1;
        c |= c >>> 1;
        c |= c >>> 2;
        c |= c >>> 4;
        c |= c >>> 8;
        c |= c >>> 16;
        return (c < 0) ? 1 : (c >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : c + 1;
    }
}