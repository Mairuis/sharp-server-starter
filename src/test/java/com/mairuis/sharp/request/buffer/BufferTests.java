package com.mairuis.sharp.request.buffer;

import com.mairuis.sharp.request.TestRequest;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mairuis
 * @since 2021/6/3
 */
public class BufferTests {

    @Test
    public void benchmark() {
        final RequestDispatcher requestDispatcher = new RequestRingDispatcher(32);
        final int cpuNumber = Runtime.getRuntime().availableProcessors();
        final int ioThreadCount = cpuNumber * 2;
        final int serviceThreadCount = cpuNumber + 1;
        final ExecutorService ioThreads = Executors.newFixedThreadPool(ioThreadCount);
        final ExecutorService serviceThreads = Executors.newFixedThreadPool(serviceThreadCount);
        for (int i = 0; i < ioThreadCount; i++) {
            ioThreads.execute(() -> {
                for (int j = 0; j < 100000; j++) {
                    requestDispatcher.publish(new TestRequest("/benchmark", "this is a text".getBytes(StandardCharsets.UTF_8)));
                }
            });
        }
    }
}
