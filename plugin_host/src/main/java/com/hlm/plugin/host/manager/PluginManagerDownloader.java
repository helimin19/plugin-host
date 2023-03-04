package com.hlm.plugin.host.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hlm.plugin.host.HostApplication;
import com.hlm.plugin.host.entity.PluginManagerEntity;
import com.hlm.plugin.lib.http.ResquestCallback;
import com.hlm.plugin.lib.http.RetrofitProxy;
import com.tencent.shadow.core.common.Logger;
import com.tencent.shadow.core.common.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 更新插件
 */
public class PluginManagerDownloader {

    private Context context;
    private LinkedList<PluginManagerEntity> queue;
    private ResquestCallback<Map<PluginManagerEntity, File>> callback;
    private Map<PluginManagerEntity, File> result = new HashMap<>();

    public PluginManagerDownloader(Context context, LinkedList<PluginManagerEntity> queue, ResquestCallback<Map<PluginManagerEntity, File>> callback) {
        this.context = context;
        this.queue = queue;
        this.callback = callback;
    }

    /**
     * 开始
     */
    public void start() {
        if (this.queue.size() == 0) {
            this.end();
            return;
        }

        PluginManagerEntity pm = this.queue.removeFirst();
        update(context, pm, new ResquestCallback<File>() {
            @Override
            public void successed(File data) {
                result.put(pm, data);
                start();
            }

            @Override
            public void failed(String message) {
                start();
            }
        });
    }

    /**
     * 完成了
     */
    private void end() {
        if ( this.callback != null) {
            this.callback.successed(result);
        }
    }

    /**
     * 列新插件管理APK
     *
     * @param context 上下文
     * @param item    插件管理信息
     */
    public void update(Context context, PluginManagerEntity item, ResquestCallback<File> callback) {
        HostApplication.getLogger().debug("开始下载插件管理apk:" + item.getCode());

        Retrofit retrofit = RetrofitProxy.instance().getRetrofitProvider().getRetrofit(context);
        PluginManagerApi api = retrofit.create(PluginManagerApi.class);
        String url = PluginManagerConfig.instance().getUrlUpdate();
        Call<ResponseBody> call = api.update(url, item);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    HostApplication.getLogger().debug("下载插件管理apk成功");

                    File file = save(context, item, response.body());
                    callback.successed(file);
                } else {
                    HostApplication.getLogger().debug("下载插件管理apk失败:" + response.message());

                    callback.failed(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                HostApplication.getLogger().debug("下载插件管理apk请求失败:" + t.getMessage());

                callback.failed(t.getMessage());
            }
        });
    }

    /**
     * 保存插件管理
     * @param context 上下文
     * @param item 插件管理信息
     * @param body apk文件流
     * @return apk文件
     */
    private File save(Context context, PluginManagerEntity item, ResponseBody body) {
        File path =PluginManagerService.getPluginManagerPath(context);
        // 删除以前的版本
        File[] dels = path.listFiles(file -> {
            if (!file.isFile()) {
                return false;
            }
            String name = file.getName();
            if (TextUtils.isEmpty(name) || name.indexOf(".") == -1) {
                return false;
            }
            String ext = name.substring(name.lastIndexOf("."));
            if (!"apk".equalsIgnoreCase(ext)) {
                return false;
            }
            if (name.startsWith(item.getCode() + ".")) {
                return true;
            }
            return false;
        });
        if (dels != null && dels.length > 0) {
            for (File del : dels) {
                try {
                    del.delete();
                } catch (Exception e) {
                    Log.e("删除老版本的插件管理出错:" + item.getCode(), e.getMessage());
                }
            }
        }

        String name = item.getFileName();
        File file = new File(path + File.separator + name);
        try (
                InputStream inputStream = body.byteStream();
                OutputStream outputStream = new FileOutputStream(file);
        ) {
            byte[] fileReader = new byte[4096];
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            return file;
        } catch (IOException e) {
            Log.e("插件管理APK存储出错:" + item.getCode(), e.getMessage());
        }
        return null;
    }

}
