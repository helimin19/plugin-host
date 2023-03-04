package com.hlm.plugin.host.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.hlm.plugin.host.HostApplication;
import com.hlm.plugin.host.exception.PluginException;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;

/**
 * 插件服务
 */
public class PluginService {

    /**
     * 启动插件
     * @param context 上下文
     * @param managerCode 插件管理的编码
     * @param fromId 标识符，插件管理中使用这个标识是什么
     * @param bundle 参数
     * @param callback 回调
     */
    public static void enter(Context context, String managerCode, long fromId, Bundle bundle, PluginEnterCallback callback) throws PluginException {
        PluginManager pluginManager = HostApplication.getPluginManager(managerCode);
        if (pluginManager != null) {
            pluginManager.enter(context, fromId, bundle, new EnterCallback() {

                @Override
                public void onShowLoadingView(View view) {
                    if (callback != null) {
                        callback.onShowLoadingView(view);
                    }
                }

                @Override
                public void onCloseLoadingView() {
                    if (callback != null) {
                        callback.onCloseLoadingView();
                    }
                }

                @Override
                public void onEnterComplete() {
                    if (callback != null) {
                        callback.onEnterComplete();
                    }
                }
            });
        } else {
            throw new PluginException("插件管理的编码不存在.");
        }
    }
}
