package com.mairuis.sharp.request.buffer;

import com.mairuis.sharp.request.Request;
import com.mairuis.sharp.request.TestRequest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mairuis
 * @since 2021/6/3
 */
public class BufferTests {

    @Test
    public void benchmark() {
        final RequestBuffer requestBuffer = new RequestRingBuffer(32);
        final int cpuNumber = Runtime.getRuntime().availableProcessors();
        final int ioThreadCount = cpuNumber * 2;
        final int serviceThreadCount = cpuNumber + 1;
        final ExecutorService ioThreads = Executors.newFixedThreadPool(ioThreadCount);
        final ExecutorService serviceThreads = Executors.newFixedThreadPool(serviceThreadCount);
        for (int i = 0; i < ioThreadCount; i++) {
            ioThreads.execute(() -> {
                for (int j = 0; j < 100000; j++) {
                    requestBuffer.buffered(new TestRequest("/benchmark", "this is a text".getBytes(StandardCharsets.UTF_8)));
                }
            });
        }

        for (int i = 0; i < serviceThreadCount; i++) {
            serviceThreads.execute(() -> {
                final ThreadLocalRandom current = ThreadLocalRandom.current();
                for (; ; ) {
                    final Request request = requestBuffer.pull();
                    try {
                        Thread.sleep(current.nextInt(0, 60));
                    } catch (InterruptedException e) {

                    }
                }
            });
        }
    }
}
