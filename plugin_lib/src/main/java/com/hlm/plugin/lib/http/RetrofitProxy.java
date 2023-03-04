package com.hlm.plugin.lib.http;

/**
 * Retrofit代理
 */
public class RetrofitProxy {

    private static volatile RetrofitProxy config;

    public static RetrofitProxy instance() {
        if (config == null) {
            config = new RetrofitProxy();
        }
        return config;
    }

    private RetrofitProxy() {
    }

    // Retrofit提供者
    private RetrofitProvider retrofitProvider = null;

    /**
     * 设置Retrofit提供者
     * @param provider Retrofit提供者
     * @return RetrofitProxy
     */
    public RetrofitProxy setRetrofitProvider(RetrofitProvider provider) {
        retrofitProvider = provider;
        return this;
    }

    /**
     * 取得Retrofit提供者
     * @return Retrofit提供者
     */
    public RetrofitProvider getRetrofitProvider() {
        return retrofitProvider;
    }

}
