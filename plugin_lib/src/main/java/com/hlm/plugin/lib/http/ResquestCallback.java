package com.hlm.plugin.lib.http;

/**
 * 请求回调
 * @param <T>
 */
public interface ResquestCallback<T> {

    /**
     * 检测成功
     * @param data
     */
    void successed(T data);

    /**
     * 检测失败
     * @param message
     */
    void failed(String message);
}
