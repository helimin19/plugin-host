package com.hlm.plugin.host;

import android.content.Context;

import com.hlm.plugin.lib.http.RetrofitProvider;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient implements RetrofitProvider {
    @Override
    public Retrofit getRetrofit(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.66:8080/examples/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
