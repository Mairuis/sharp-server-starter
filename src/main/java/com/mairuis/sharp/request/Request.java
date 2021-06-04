package com.mairuis.sharp.request;

/**
 * 一次服务请求的抽象
 *
 * @author Mairuis
 * @since 2021/6/1
 */
public interface Request {

    /**
     * 获取请求目的地
     *
     * @return
     */
    String getUrl();

    /**
     * 请求附带内容
     *
     * @return
     */
    byte[] getContent();

}
