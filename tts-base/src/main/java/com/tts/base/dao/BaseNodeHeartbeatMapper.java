package com.tts.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tts.base.domain.BaseNodeHeartbeat;

import java.util.List;

/**
 * 节点心跳 数据层
 */
public interface BaseNodeHeartbeatMapper extends BaseMapper<BaseNodeHeartbeat> {

    List<BaseNodeHeartbeat> selectInServerList(List<String> serverIpList);

    BaseNodeHeartbeat selectBaseNodeHeartbeatById(String localIp);

    void updateBaseNodeHeartbeatById(BaseNodeHeartbeat heartbeat);
}
