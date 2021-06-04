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
public class RequestRingBuffer implements RequestBuffer {

    private static final int MAXIMUM_CAPACITY = 2048;

    private final Request[] ring;

    private final AtomicInteger sequenceId = new AtomicInteger(0);

    public RequestRingBuffer(int capacity) {
        this.ring = new Request[ringSizeOf(capacity)];
    }

    private int nextId() {
        return Math.abs(sequenceId.getAndIncrement());
    }

    @Override
    public Request pull() {
        return null;
    }

    @Override
    public Request get() {
        return null;
    }

    @Override
    public boolean buffered(Request request) {
        return false;
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
