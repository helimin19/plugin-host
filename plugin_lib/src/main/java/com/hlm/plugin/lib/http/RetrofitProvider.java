package com.hlm.plugin.lib.http;

import android.content.Context;

import androidx.annotation.Keep;

import retrofit2.Retrofit;

/**
 * Retrofit提供者
 */
@Keep
public interface RetrofitProvider {

    /**
     * 取得Retrofit
     * @return Retrofit
     */
    Retrofit getRetrofit(Context context);

}
