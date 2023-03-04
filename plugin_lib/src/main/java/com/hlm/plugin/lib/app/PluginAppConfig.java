package com.hlm.plugin.lib.app;

import androidx.annotation.Keep;

/**
 * 插件应用配置
 */
@Keep
public class PluginAppConfig {

    private static volatile PluginAppConfig config;

    public static PluginAppConfig instance() {
        if (config == null) {
            config = new PluginAppConfig();
        }
        return config;
    }

    // baseUrl
    private String baseUrl;

    // 检测插件应用更新的URL
    private String urlCheck;

    // 更新插件应用的URL
    private String urlUpdate;

    private PluginAppConfig() {
    }

    public PluginAppConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public PluginAppConfig setUrlCheck(String urlCheck) {
        this.urlCheck = urlCheck;
        return this;
    }

    public PluginAppConfig setUrlUpdate(String urlUpdate) {
        this.urlUpdate = urlUpdate;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }


    public String getUrlCheck() {
        return urlCheck;
    }

    public String getUrlUpdate() {
        return urlUpdate;
    }

}
