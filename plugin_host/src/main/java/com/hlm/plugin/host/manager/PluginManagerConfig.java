package com.hlm.plugin.host.manager;

import com.hlm.plugin.host.http.RetrofitProxy;

/**
 * 插件管理配置
 */
public class PluginManagerConfig {

    private static volatile PluginManagerConfig config;

    public static PluginManagerConfig instance() {
        if (config == null) {
            config = new PluginManagerConfig();
        }
        return config;
    }

    // Retrofit代理
    private RetrofitProxy retrofitProxy = null;

    // 检测插件管理更新的URL
    private String urlCheck;

    // 更新插件管理的URL
    private String urlUpdate;

    private PluginManagerConfig() {
    }

    public PluginManagerConfig setRetrofitProxy(RetrofitProxy proxy) {
        retrofitProxy = proxy;
        return this;
    }

    public PluginManagerConfig setUrlCheck(String urlCheck) {
        this.urlCheck = urlCheck;
        return this;
    }

    public PluginManagerConfig setUrlUpdate(String urlUpdate) {
        this.urlUpdate = urlUpdate;
        return this;
    }

    public RetrofitProxy getRetrofitProxy() {
        if (retrofitProxy == null) {
            throw new NullPointerException("Retrofit Proxy cannot be empty");
        }
        return retrofitProxy;
    }

    public String getUrlCheck() {
        return urlCheck;
    }

    public String getUrlUpdate() {
        return urlUpdate;
    }

}
