package com.hlm.plugin.host.manager;

import com.hlm.plugin.host.entity.PluginManagerEntity;

import java.util.List;

/**
 * 管理插件更新结果
 */
public class PluginManagerUpdateResult {

    // 需要新增或更新的插件管理信息
    private List<PluginManagerEntity> updates;

    // 需要删除的插件管理信息
    private List<PluginManagerEntity> deletes;

    public List<PluginManagerEntity> getUpdates() {
        return updates;
    }

    public void setUpdates(List<PluginManagerEntity> updates) {
        this.updates = updates;
    }

    public List<PluginManagerEntity> getDeletes() {
        return deletes;
    }

    public void setDeletes(List<PluginManagerEntity> deletes) {
        this.deletes = deletes;
    }
}
