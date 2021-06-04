package com.mairuis.sharp.request.worker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 从请求缓冲池主动 pull 任务执行
 *
 * @author Mairuis
 * @since 2021/6/1
 */
public class WorkerThread extends Thread {

    /**
     * 工作线程的Id自增
     */
    private static final AtomicInteger WORKER_ID = new AtomicInteger(1);

    public WorkerThread() {
        super("Service-Worker-" + WORKER_ID.getAndIncrement());
    }

    @Override
    public void run() {

    }
}
