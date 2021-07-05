package com.mairuis.sharp.request.worker;

import java.util.function.Supplier;

/**
 * @author Mairuis
 * @since 2021/6/1
 */
public class WorkerThreadGroup {

    private final WorkerThread[] threads;

    public WorkerThreadGroup(int threadCount, Supplier<? extends WorkerThread> threadFactory) {
        this.threads = new WorkerThread[threadCount];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = threadFactory.get();
        }
    }

}
