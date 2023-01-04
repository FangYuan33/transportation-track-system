package com.tts.base.zookeeper;

import com.tts.base.service.BaseServerStateLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * zookeeper指定路径的监听器 用于leader节点使用
 *
 * @author FangYuan
 * @since 2022-12-29 20:00:23
 */
@Slf4j
public class ZkTreeCacheListener implements TreeCacheListener {

    private final BaseServerStateLogService baseServerStateLogService;

    private final String serviceName;

    public ZkTreeCacheListener(BaseServerStateLogService baseServerStateLogService, String serviceName) {
        this.baseServerStateLogService = baseServerStateLogService;
        this.serviceName = serviceName;
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) {
        // 节点被移除时
        if (TreeCacheEvent.Type.NODE_REMOVED.equals(event.getType())) {
            log.warn("TTS Node {} has been removed!!!", serviceName);

            baseServerStateLogService.saveNewState(serviceName, event.getType().name());
        }
    }
}
