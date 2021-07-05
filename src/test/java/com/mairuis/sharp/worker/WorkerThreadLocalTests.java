package com.mairuis.sharp.worker;

import com.mairuis.sharp.request.worker.WorkerThread;
import com.mairuis.sharp.request.worker.WorkerThreadLocal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mairuis
 * @since 2021/7/5
 */
public class WorkerThreadLocalTests {

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    public void testMultipleSetAndGet() throws InterruptedException {
        final WorkerThreadLocal<String> threadLocal = new WorkerThreadLocal<>();
        final WorkerThread[] threads = new WorkerThread[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new WorkerThread(() -> {
                threadLocal.set(Thread.currentThread().getName());
                assertEquals(threadLocal.get(), Thread.currentThread().getName());
            });
        }
        for (WorkerThread thread : threads) {
            thread.start();
        }
        for (WorkerThread thread : threads) {
            thread.join();
        }
    }

}
