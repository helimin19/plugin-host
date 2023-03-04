package com.hlm.plugin.host;

import static android.os.Process.myPid;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.hlm.plugin.host.entity.PluginManagerEntity;
import com.hlm.plugin.host.manager.FixedPathPmUpdater;
import com.hlm.plugin.host.manager.PluginManagerService;
import com.hlm.plugin.host.manager.PluginManagerUpdateResult;
import com.hlm.plugin.lib.http.ResquestCallback;
import com.tencent.shadow.core.common.Logger;
import com.tencent.shadow.core.common.LoggerFactory;
import com.tencent.shadow.dynamic.host.DynamicPluginManager;
import com.tencent.shadow.dynamic.host.DynamicRuntime;
import com.tencent.shadow.dynamic.host.PluginManager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 宿主Application
 */
public class HostApplication {

    /**
     * 更新回调
     */
    public interface UpdateCallback {
        // 完成
        void finished();
    }

    /**
     * 这个PluginManager对象在Manager升级前后是不变的。它内部持有具体实现，升级时更换具体实现。
     */
    private static Map<String, PluginManager> pluginManagerList = new HashMap<>();

    /**
     * 取得指定名称的插件管理
     * @param code 插件管理的编码
     * @return 插件管理对象
     */
    public static PluginManager getPluginManager(String code) {
        return pluginManagerList.get(code);
    }

    /**
     * 创建应用时调用
     * @param application 应用
     */
    public static void onApplicationCreate(Application application) {
        // Log接口Manager也需要使用，所以主进程也初始化。
        LoggerFactory.setILoggerFactory(new AndroidLoggerFactory());

        if (isProcess(application, ":plugin")) {
            // 在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的runtime，
            // 为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
            // 因此这里恢复加载上一次的runtime
            DynamicRuntime.recoveryRuntime(application);
        }
    }

    /**
     * 更新插件管理 - 一般放在启动界面，不要放在applicagtion中，因为这里需要下载更新
     */
    public static void updatePluginManagers(Context context, UpdateCallback callback) {
        getLogger().debug("plugin-manager-开始检测插件管理...");

        // 第一步: 获取新增了哪些管理插件；更新了哪些管理插件; 删除了哪些管理插件
        PluginManagerService service = new PluginManagerService();
        service.check(context, new ResquestCallback<PluginManagerUpdateResult>() {
            @Override
            public void successed(PluginManagerUpdateResult result) {
                getLogger().debug("plugin-manage-检测插件管理成功, 开始更新检测插件管理成功");

                // 删除
                service.deletes(context, result.getDeletes());

                // 更新
                service.updates(context, result.getUpdates(), new ResquestCallback<Map<PluginManagerEntity, File>>() {
                    @Override
                    public void successed(Map<PluginManagerEntity, File> data) {
                        getLogger().debug("plugin-manager-更新检测插件管理成功");

                        loadAllPluginManagers(context);

                        callback.finished();
                    }

                    @Override
                    public void failed(String message) {
                        getLogger().debug("plugin-manager-更新检测插件管理失败:" + message);

                        loadAllPluginManagers(context);

                        callback.finished();
                    }
                });
            }

            @Override
            public void failed(String message) {
                getLogger().debug("plugin-manage-检测插件管理失败:" + message);

                loadAllPluginManagers(context);

                callback.finished();
            }
        });
    }

    /**
     * 加载所有的插件管理APK
     * @param context 上下文
     */
    private static void loadAllPluginManagers(Context context) {
        Map<PluginManagerEntity, File> pluginManagerApkPathMap = PluginManagerService.getMap(context);

        getLogger().debug("plugin-manage-需要安装的插件管理个数:" + pluginManagerApkPathMap.size());


        initPluginManagers(pluginManagerApkPathMap);
    }

    /**
     * 初使化插件管理
     */
    public static void initPluginManagers(Map<PluginManagerEntity, File> pluginManagerApkPathMap) {
        if (pluginManagerApkPathMap == null) {
            return;
        }
        Iterator<Map.Entry<PluginManagerEntity, File>> iterator = pluginManagerApkPathMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<PluginManagerEntity, File> entry = iterator.next();
            initPluginManager(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 初使化插件管理
     */
    public static void initPluginManager(PluginManagerEntity entity, File pluginManagerApkFile) {
        FixedPathPmUpdater fixedPathPmUpdater = new FixedPathPmUpdater(pluginManagerApkFile);
        boolean needWaitingUpdate
                = fixedPathPmUpdater.wasUpdating() // 之前正在更新中，暗示更新出错了，应该放弃之前的缓存
                || fixedPathPmUpdater.getLatest() == null; // 没有本地缓存
        Future<File> update = fixedPathPmUpdater.update();
        if (needWaitingUpdate) {
            try {
                update.get(); // 这里是阻塞的，需要业务自行保证更新Manager足够快。
            } catch (Exception e) {
                throw new RuntimeException("程序不容错", e);
            }
        }

        getLogger().debug("plugin-manage-初使化插件管理:" + entity.getCode());

        pluginManagerList.put(entity.getCode(), new DynamicPluginManager(fixedPathPmUpdater));
    }

    /**
     * 是否指定进程
     * @param context 上下文
     * @param processName 进程名称
     * @return true/false
     */
    private static boolean isProcess(Context context, String processName) {
        String currentProcName = "";
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == myPid()) {
                currentProcName = processInfo.processName;
                break;
            }
        }

        return currentProcName.endsWith(processName);
    }

    public static Logger getLogger() {
        return LoggerFactory.getILoggerFactory().getLogger("default");
    }

}
