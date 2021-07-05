package com.mairuis.sharp.request.worker;

import java.util.Arrays;

/**
 * 线程本地缓存数组
 *
 * @author Mairuis
 * @since 2021/7/3
 */
public class WorkerThreadLocalArray {

    /**
     * 代表一个未赋值的槽位
     */
    public static final Object UNSET = new Object();

    private Object[] array;

    public static final int MAX_THREAD_LOCAL_COUNT = 1 << 30;

    private static final ThreadLocal<WorkerThreadLocalArray> SLOW_THREAD_LOCAL_ARRAY = ThreadLocal.withInitial(WorkerThreadLocalArray::new);

    public WorkerThreadLocalArray() {
        this.array = new Object[32];
        Arrays.fill(array, UNSET);
    }

    public Object get(int index) {
        if (index >= array.length) {
            return UNSET;
        }
        return array[index];
    }

    public boolean set(int index, Object value) {
        Object[] oldArray = array;
        if (index < oldArray.length) {
            Object oldValue = oldArray[index];
            oldArray[index] = value;
            return oldValue == UNSET;
        } else {
            final int oldCapacity = oldArray.length;
            int newCapacity = arraySizeOf(index);
            Object[] newArray = Arrays.copyOf(oldArray, newCapacity);
            Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
            array = newArray;
            return true;
        }
    }

    private int arraySizeOf(int index) {
        int newCapacity = index - 1;
        newCapacity |= newCapacity >>> 1;
        newCapacity |= newCapacity >>> 2;
        newCapacity |= newCapacity >>> 4;
        newCapacity |= newCapacity >>> 8;
        newCapacity |= newCapacity >>> 16;
        return index < 0 ? 1 : (newCapacity >= MAX_THREAD_LOCAL_COUNT) ? MAX_THREAD_LOCAL_COUNT : newCapacity + 1;
    }

    public boolean contain(int index) {
        return array[index] != UNSET;
    }

    public void destroy() {
        this.array = null;

        SLOW_THREAD_LOCAL_ARRAY.remove();
    }

    public static void destroyAll() {
        final WorkerThreadLocalArray localArray = getIfPresent();
        if (localArray != null) {
            localArray.destroy();
        }
    }

    public static WorkerThreadLocalArray getIfPresent() {
        final Thread thread = Thread.currentThread();
        if (thread instanceof WorkerThread) {
            return ((WorkerThread) thread).getThreadLocalArray();
        }
        return null;
    }

    public static WorkerThreadLocalArray getOrCreate() {
        final Thread thread = Thread.currentThread();
        if (thread instanceof WorkerThread) {
            return getOrCreate((WorkerThread) thread);
        } else {
            //非 WorkerThread 时则使用JDK的 ThreadLocal
            return SLOW_THREAD_LOCAL_ARRAY.get();
        }
    }

    private static WorkerThreadLocalArray getOrCreate(WorkerThread thread) {
        WorkerThreadLocalArray threadLocalArray;
        if ((threadLocalArray = thread.getThreadLocalArray()) == null) {
            thread.setThreadLocalArray((threadLocalArray = new WorkerThreadLocalArray()));
        }
        return threadLocalArray;
    }
}
