package com.mairuis.sharp.request.buffer;

import com.mairuis.sharp.request.Request;

/**
 * 请求缓冲池
 *
 * @author Mairuis
 * @since 2021/6/3
 */
public interface RequestBuffer {

    /**
     * 获取一个待处理的请求，如果缓冲池为空则阻塞直到有新的请求
     *
     * @return
     */
    Request pull();

    /**
     * 获取一个请求，如果缓冲池为空则返回空
     *
     * @return
     */
    Request get();

    boolean buffered(Request request);

}
