package com.hlm.plugin.host.entity;

/**
 * 插件管理信息
 */
public class PluginManagerEntity {
    // 插件管理的编码
    private String code;

    // 插件管理APK的版本
    private String version;

    public PluginManagerEntity() {
    }

    public PluginManagerEntity(String code, String version) {
        this.code = code;
        this.version = version;
    }

    /**
     * 取得文件名称
     * @return 文件名称
     */
    public String getFileName() {
        return this.code + "." + this.version + ".apk";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
