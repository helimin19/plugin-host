package com.hlm.plugin.lib.http;

import android.content.Context;

import retrofit2.Retrofit;

/**
 * Retrofit提供者
 */
public interface RetrofitProvider {

    /**
     * 取得Retrofit
     * @return Retrofit
     */
    Retrofit getRetrofit(Context context);

}
