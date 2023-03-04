package com.hlm.plugin.host;

import android.app.Application;

import com.hlm.plugin.host.manager.PluginManagerConfig;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PluginManagerConfig.instance().setRetrofitProxy(new MyRetrofitProxy()).setUrlCheck("plugin/manager/check.txt").setUrlUpdate("MainPluginManager-debug.apk");

        HostApplication.onApplicationCreate(this);
    }
}
