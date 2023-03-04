package com.hlm.plugin.lib.http;

import androidx.annotation.Keep;

/**
 * 请求回调
 * @param <T>
 */
@Keep
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
