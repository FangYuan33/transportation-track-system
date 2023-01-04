package com.tts.base.service;

/**
 * 节点心跳日志服务层
 *
 * @author FangYuan
 * @since 2023-01-03 20:07:05
 */
public interface BaseNodeHeartbeatLogService {
    /**
     * 追加节点服务心跳日志流水
     */
    void appendHeartbeatLog(String serviceName);
}
