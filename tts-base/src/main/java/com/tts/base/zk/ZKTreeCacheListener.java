package com.tts.base.zk;

import com.tts.base.service.BaseServerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * 用于zk的master节点 监听 指定路径变化的监听器
 *
 * @author FangYuan
 * @since 2022-12-21 21:02:42
 */
@Slf4j
public class ZKTreeCacheListener implements TreeCacheListener {

    private String path;

    private BaseServerService baseServerService;

    public ZKTreeCacheListener(String path, BaseServerService baseServerService) {
        this.path = path;
        this.baseServerService = baseServerService;
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) {
        ChildData eventData = event.getData();

        // 处理宕机节点(被删除节点) 上面的 报警任务运行状态
        if (TreeCacheEvent.Type.NODE_REMOVED.equals(event.getType())) {
            String clientName = "empty";
            if (eventData != null && eventData.getPath() != null) {
                clientName = eventData.getPath().replace(path + "/", "");
            }
            log.warn("zk cache listener " + new String(eventData.getData()) + ":" + "[" + clientName + "]" + event.getType());
            // 记录节点事件
            if (baseServerService != null) {
                baseServerService.saveServerLog(clientName, event.getType().name());
            }
        }
    }
}
