package com.mairuis.sharp.request.buffer;

import com.mairuis.sharp.request.Request;

/**
 * 请求缓冲池
 *
 * @author Mairuis
 * @since 2021/6/3
 */
public interface RequestDispatcher {

    /**
     * 发布一个请求
     *
     * @param request
     * @return
     */
    void publish(Request request);

}
