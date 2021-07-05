package com.mairuis.sharp.request.worker;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 基于数组实现的线程本地缓存，与 {@link ThreadLocal} 的区别在于使用数组作为储存结构可实现快速访问和写入以及对缓存行结构的利用。
 *
 * @author Mairuis
 * @since 2021/6/28
 */
public class WorkerThreadLocal<T> {

    private final int id;
    private final Supplier<T> supplier;

    private static final AtomicInteger ID = new AtomicInteger(1);

    public WorkerThreadLocal() {
        this.id = nextId();
        this.supplier = null;
    }

    public WorkerThreadLocal(Supplier<T> supplier) {
        this.id = nextId();
        this.supplier = supplier;
    }

    /**
     * 获取值
     * 如果未初始化则调用初始化方法创建一个
     * 如果没有初始化方法则返回null
     *
     * @return
     */
    public T get() {
        final WorkerThreadLocalArray localArray = WorkerThreadLocalArray.getIfPresent();
        if (localArray == null) {
            return null;
        }
        Object value = localArray.get(id);
        if (value == WorkerThreadLocalArray.UNSET) {
            if (supplier == null) {
                return null;
            }
            value = supplier.get();
            localArray.set(id, value);
        }
        return value == null ? null : (T) value;
    }

    /**
     * 设置一个值
     *
     * @param value
     * @return
     */
    public boolean set(T value) {
        final WorkerThreadLocalArray localArray = WorkerThreadLocalArray.getOrCreate();
        return localArray.set(id, value);
    }

    private static int nextId() {
        final int nextId = ID.getAndIncrement();
        if (nextId > WorkerThreadLocalArray.MAX_THREAD_LOCAL_COUNT) {
            ID.decrementAndGet();
            throw new IllegalStateException("too many thread local!!!");
        }
        return nextId;
    }
}