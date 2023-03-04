package com.hlm.plugin.host.app;

import android.view.View;

import com.tencent.shadow.dynamic.host.EnterCallback;

/**
 * 插件进入回调
 */
public interface PluginEnterCallback {

    void onShowLoadingView(View view);

    void onCloseLoadingView();

    void onEnterComplete();
}
