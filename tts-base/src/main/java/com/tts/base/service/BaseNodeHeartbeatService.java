package com.tts.base.service;


import com.tts.base.domain.BaseNodeHeartbeat;

import java.util.List;

/**
 * 节点心跳service
 *
 * @author FangYuan
 * @since 2022-12-22 11:04:57
 */
public interface BaseNodeHeartbeatService {

    /**
     * 启动节点心跳
     * 该service 必须先执行这个方法
     */
    void startHearbeat();

    /**
     * 更新心跳时间
     */
    void updateHeartbeat();

    /**
     * 初始心跳记录,
     * 即如果该节点的心跳记录不存在则插入
     * 如果该节点的心跳记录存在忽略
     */
    void initHeartbeat();

    /**
     * 追加心跳流水
     */
    void appendNodeHeartbeatLog(BaseNodeHeartbeat heartbeat);

    /**
     * 根据server ip 列表查询 节点心跳信息
     */
    List<BaseNodeHeartbeat> queryInServerList(List<String> serverIpList);


    /**
     * 判断这个节点是否心跳超时了
     * 判断办法就是先判断是否心跳没有超时然后取反
     * 心跳超时的条件的 与当前时间的差大于两次心跳时间
     */
    boolean isTimeout(BaseNodeHeartbeat baseNodeHeartbeat);
}
