package com.hlm.plugin.host.http;

import android.content.Context;

import retrofit2.Retrofit;

/**
 * Retrofit代理
 */
public interface RetrofitProxy {

    /**
     * 取得Retrofit
     * @return Retrofit
     */
    Retrofit getRetrofit(Context context);

}
