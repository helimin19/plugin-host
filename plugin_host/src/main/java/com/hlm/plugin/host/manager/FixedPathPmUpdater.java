package com.hlm.plugin.host.manager;

import com.tencent.shadow.dynamic.host.PluginManagerUpdater;

import java.io.File;
import java.util.concurrent.Future;

/**
 * 这个Updater没有任何升级能力。直接将指定路径作为其升级结果。
 */
public class FixedPathPmUpdater implements PluginManagerUpdater {

    final private File apk;

    public FixedPathPmUpdater(File apk) {
        this.apk = apk;
    }

    /**
     * @return <code>true</code>表示之前更新过程中意外中断了
     */
    @Override
    public boolean wasUpdating() {
        return false;
    }

    /**
     * 更新
     *
     * @return 当前最新的PluginManager，可能是之前已经返回过的文件，但它是最新的了。
     */
    @Override
    public Future<File> update() {
        return null;
    }

    /**
     * 获取本地最新可用的
     *
     * @return <code>null</code>表示本地没有可用的
     */
    @Override
    public File getLatest() {
        return this.apk;
    }

    /**
     * 查询是否可用
     *
     * @param file PluginManagerUpdater返回的file
     * @return <code>true</code>表示可用，<code>false</code>表示不可用
     */
    @Override
    public Future<Boolean> isAvailable(File file) {
        return null;
    }

}