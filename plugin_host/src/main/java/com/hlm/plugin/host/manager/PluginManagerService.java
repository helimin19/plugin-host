package com.hlm.plugin.host.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hlm.plugin.host.entity.PluginManagerEntity;
import com.hlm.plugin.lib.http.ResquestCallback;
import com.hlm.plugin.lib.http.Result;
import com.hlm.plugin.lib.http.RetrofitProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 插件管理服务
 */
public class PluginManagerService {

    // 插件管理的路径
    private static final String PATH_PLUGIN_MANAGER = "plugin/manager";

    /**
     * 检测插件管理更新
     *
     * @param context  上下文
     * @param callback 请求回调
     */
    public void check(Context context, ResquestCallback<PluginManagerUpdateResult> callback) {
        Retrofit retrofit = RetrofitProxy.instance().getRetrofitProvider().getRetrofit(context);
        PluginManagerApi api = retrofit.create(PluginManagerApi.class);
        List<PluginManagerEntity> versions = getVersions(context);
        String url = PluginManagerConfig.instance().getUrlCheck();
        Call<Result<PluginManagerUpdateResult>> call = api.check(url, versions);
        call.enqueue(new Callback<Result<PluginManagerUpdateResult>>() {
            @Override
            public void onResponse(Call<Result<PluginManagerUpdateResult>> call, Response<Result<PluginManagerUpdateResult>> response) {
                if (response.isSuccessful()) {
                    Result<PluginManagerUpdateResult> result = response.body();
                    if (result.isSuccessful()) {
                        callback.successed(result.getData());
                    } else {
                        callback.failed(result.getMessage());
                    }
                } else {
                    callback.failed(response.message());
                }
            }

            @Override
            public void onFailure(Call<Result<PluginManagerUpdateResult>> call, Throwable t) {
                callback.failed(t.getMessage());
            }
        });
    }

    /**
     * 更新插件
     * @param context 上下文
     * @param items 要更新的插件管理信息
     * @param callback 回调
     */
    public void updates(Context context, List<PluginManagerEntity> items, ResquestCallback<Map<PluginManagerEntity, File>> callback) {
        if (items == null || items.size() == 0) {
            callback.successed(new HashMap<>());
            return;
        }
        LinkedList<PluginManagerEntity> queue = new LinkedList<>();
        for (PluginManagerEntity item : items) {
            queue.push(item);
        }

        PluginManagerDownloader updater = new PluginManagerDownloader(context, queue, callback);
        updater.start();
    }

    /**
     * 删除
     * @param context 上下文
     * @param items 要删除的
     */
    public void deletes(Context context, List<PluginManagerEntity> items) {
        // 删除
        if (items != null && items.size() > 0) {
            for (PluginManagerEntity del : items) {
                deleteByCode(context, del.getCode(), null);
            }
        }
    }

    /**
     * 取得所有插件管理apk
     * @param context 上下文
     * @return 所有插件管理apk
     */
    public static File[] getAll(Context context) {
        File path = getPluginManagerPath(context);
        return path.listFiles(file -> {
            if (!file.isFile()) {
                return false;
            }
            String name = file.getName();
            if (TextUtils.isEmpty(name) || name.indexOf(".") == -1) {
                return false;
            }
            String ext = name.substring(name.lastIndexOf(".") + 1);
            if ("apk".equalsIgnoreCase(ext)) {
                return true;
            }
            return false;
        });
    }

    /**
     * 取得所有插件管理apk
     * @param context 上下文
     * @return 所有插件管理apk
     */
    public static Map<PluginManagerEntity, File> getMap(Context context) {
        Map<PluginManagerEntity, File> map = new HashMap<>();

        File[] files = getAll(context);
        if (files == null || files.length == 0) {
            return map;
        }

        for (File file : files) {
            String name = file.getName().substring(0, file.getName().lastIndexOf("."));
            int position = name.indexOf(".");
            if (position > 0) {
                String code = name.substring(0, position);
                String version = name.substring(position + 1);
                map.put(new PluginManagerEntity(code, version), file);
            }
        }
        return map;
    }

    /**
     * 取得插件管理的版本
     *
     * @param context 上下文
     * @return 所有插件管理的版本
     */
    private List<PluginManagerEntity> getVersions(Context context) {
        List<PluginManagerEntity> list = new ArrayList<>();
        File[] files = getAll(context);
        if (files == null || files.length == 0) {
            return list;
        }
        for (File file : files) {
            String name = file.getName().substring(0, file.getName().lastIndexOf("."));
            int position = name.indexOf(".");
            if (position > 0) {
                String code = name.substring(0, position);
                String version = name.substring(position + 1);
                list.add(new PluginManagerEntity(code, version));
            }
        }
        return list;
    }

    /**
     * 取得插件管理存储的目录
     *
     * @param context 上下文
     * @return 插件管理存储的目录
     */
    public static File getPluginManagerPath(Context context) {
        File root = context.getFilesDir();
        File path = new File(root, PATH_PLUGIN_MANAGER);
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }

    /**
     * 按插件管理的编码删除
     * @param context 上下文
     * @param code 按插件管理的编码
     * @param excludes 要排除的
     */
    public static void deleteByCode(Context context, String code, List<PluginManagerEntity> excludes) {
        File path = PluginManagerService.getPluginManagerPath(context);
        // 删除
        File[] dels = path.listFiles(file -> {
            if (!file.isFile()) {
                return false;
            }
            String name = file.getName();
            if (exists(excludes, name)) {
                return false;
            }
            if (TextUtils.isEmpty(name) || name.indexOf(".") == -1) {
                return false;
            }
            String ext = name.substring(name.lastIndexOf("."));
            if (!"apk".equalsIgnoreCase(ext)) {
                return false;
            }
            if (name.startsWith(code + ".")) {
                return true;
            }
            return false;
        });
        if (dels != null && dels.length > 0) {
            for (File del : dels) {
                try {
                    del.delete();
                } catch (Exception e) {
                    Log.e("删除插件管理出错:" + code, e.getMessage());
                }
            }
        }
    }

    /**
     * 是否存在
     * @param excludes 要排除的文件
     * @param name 文件名称
     * @return true/false
     */
    private static boolean exists(List<PluginManagerEntity> excludes, String name) {
        boolean res = false;
        if (excludes != null && excludes.size() > 0) {
            for (PluginManagerEntity pm : excludes) {
                if (pm.getFileName().equalsIgnoreCase(name)) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

}