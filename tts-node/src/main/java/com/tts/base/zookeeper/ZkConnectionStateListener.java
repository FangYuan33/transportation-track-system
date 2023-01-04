package com.tts.base.zookeeper;

import com.tts.base.service.BaseServerStateLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * zookeeper节点链接状态监听器
 */
@Slf4j
@Component
public class ZkConnectionStateListener implements ConnectionStateListener {

    @Autowired
    private TtsZkNode ttsZkNode;
    @Autowired
    private BaseServerStateLogService baseServerStateLogService;

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        log.info("TTS Node state has changed: {}", connectionState.toString());

        // 日志记录节点变化
        baseServerStateLogService.saveNewState(ttsZkNode.getServiceName(), connectionState.name());

        // SUSPENDED重试链接
        if (ConnectionState.SUSPENDED.equals(connectionState)) {
            log.error("Connection has been lost, wait restart!");

            // 先关闭再试试启动
            ttsZkNode.close();

            ttsZkNode.afterPropertiesSet();
            ttsZkNode.start();
        }
        // 丢失则结束该节点的生命
        if (ConnectionState.LOST.equals(connectionState)) {
            ttsZkNode.close();
        }
    }
}
