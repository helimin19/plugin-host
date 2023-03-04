package com.hlm.plugin.host;

import android.app.Application;

import com.hlm.plugin.host.manager.PluginManagerConfig;
import com.hlm.plugin.lib.http.RetrofitProxy;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitProxy.instance().setRetrofitProvider(new RetrofitClient());

        PluginManagerConfig.instance().setUrlCheck("plugin/manager/check.txt").setUrlUpdate("MainPluginManager-debug.apk");

        HostApplication.onApplicationCreate(this);
    }
}
