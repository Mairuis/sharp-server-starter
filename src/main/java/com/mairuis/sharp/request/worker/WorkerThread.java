package com.mairuis.sharp.request.worker;

/**
 * 工作线程
 * 支持 {@link WorkerThreadLocal}
 *
 * @author Mairuis
 * @since 2021/6/1
 */
public class WorkerThread extends Thread {

    private WorkerThreadLocalArray threadLocalArray;

    public WorkerThread(String name) {
        super(name);
    }

    public WorkerThread(Runnable target) {
        super(target);
    }

    @Override
    public void run() {
        try {
            super.run();
        } finally {
            WorkerThreadLocalArray.destroyAll();
        }
    }

    public WorkerThreadLocalArray getThreadLocalArray() {
        return threadLocalArray;
    }

    public void setThreadLocalArray(WorkerThreadLocalArray threadLocalArray) {
        this.threadLocalArray = threadLocalArray;
    }
}
