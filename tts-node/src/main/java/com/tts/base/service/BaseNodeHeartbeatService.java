package com.tts.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tts.base.domain.BaseNodeHeartbeat;

/**
 * 节点心跳服务层
 *
 * @author FangYuan
 * @since 2023-01-03 20:06:17
 */
public interface BaseNodeHeartbeatService extends IService<BaseNodeHeartbeat> {

    /**
     * 开启节点心跳，按照配置的时间更新心跳时间并记录心跳日志流水
     *
     * @param serviceName 服务名称
     */
    void startHeartbeat(String serviceName);
}
