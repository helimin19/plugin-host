package com.hlm.plugin.host;

import android.app.Application;
import android.util.Log;

import com.hlm.plugin.host.manager.PluginManagerConfig;
import com.hlm.plugin.lib.app.PluginAppConfig;
import com.hlm.plugin.lib.http.RetrofitProxy;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitProxy.instance().setRetrofitProvider(new RetrofitClient());

        PluginManagerConfig.instance().setUrlCheck("plugin/manager/check.txt").setUrlUpdate("plugin_manager-debug.apk");

        PluginAppConfig.instance().setBaseUrl("http://192.168.31.66:8080/examples/").setUrlCheck("plugin/app/check.txt").setUrlUpdate("plugin-debug.zip");

        HostApplication.onApplicationCreate(this);
    }
}
