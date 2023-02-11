package com.tts.runner;

import com.tts.base.service.BaseNodeHeartbeatService;
import com.tts.base.zookeeper.TtsZkNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * TTS节点启动类，随系统启动
 * 1. 启动节点
 * 2. 记录节点心跳
 *
 * @author FangYuan
 * @since 2022-12-29 15:13:48
 */
@Slf4j
@Component
public class TtsNodeRunner implements ApplicationRunner {

    @Autowired
    private TtsZkNode ttsZkNode;
    @Autowired
    private BaseNodeHeartbeatService nodeHeartbeatService;

    @Override
    public void run(ApplicationArguments args) {
        log.info(">>>>>> TTS Node Runner start >>>>>>");
        ttsZkNode.start();
        log.info("<<<<<< TTS Node Runner start over <<<<<<");

        log.info(">>>>>> TTS Node Heartbeat start >>>>>>");
        nodeHeartbeatService.startHeartbeat();
        log.info("<<<<<< TTS Node Heartbeat start over <<<<<<");
    }
}
