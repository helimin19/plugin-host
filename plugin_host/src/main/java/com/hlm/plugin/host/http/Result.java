package com.hlm.plugin.host.http;

/**
 * 响应结果包装类
 * @param <T>
 */
public class Result<T> {
    // 请求成功了，但处理结果错误的错误码
    private int code;

    // 请求错误信息
    private String message;

    // 请求结果
    private T data;

    /**
     * 请求数据是否正常
     * @return
     */
    public boolean isSuccessful() {
        if (code == 1 || code == 200) {
            return true;
        }
        return false;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
