package com.hlm.plugin.host.manager;

import com.hlm.plugin.host.entity.PluginManagerEntity;
import com.hlm.plugin.host.http.Result;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 插件管理请求API
 */
public interface PluginManagerApi {

    /**
     * 检查插件更新
     * @param url 检测插件管理是否有更新的URL地址
     * @param versions 本地插件管理的版本信息
     * @return 管理插件更新结果
     */
    @POST
    Call<Result<PluginManagerUpdateResult>> check(@Url String url, @Body List<PluginManagerEntity> versions);

    /**
     * 更新插件管理
     * @param url 更新插件管理的URL地址
     * @param item 插件管理的基本信息
     * @return 插件管理APK文件
     */
    @POST
    Call<ResponseBody> update(@Url String url, @Body PluginManagerEntity item);

}
