package com.tts.base.application;

import com.tts.base.service.BaseNodeHeartbeatService;
import com.tts.base.service.BaseServerService;
import com.tts.base.zk.BaseWorkServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BaseApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private Environment env;

    @Autowired
    private BaseServerService baseServerService;

    @Autowired
    private BaseNodeHeartbeatService baseNodeHeartbeatService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("BaseApplicationRunnerImpl run start  !!!");

        // 初始化zookeeper
        BaseWorkServer.init(env, baseServerService);
        BaseWorkServer.start();

        log.info("BaseApplicationRunnerImpl run  zookeeper end  !!!");

        // 启动节点心跳上报线程
        log.info("BaseApplicationRunnerImpl run heartbeat start  !!!");
        baseNodeHeartbeatService.startHearbeat();
        log.info("BaseApplicationRunnerImpl run heartbeat end  !!!");
    }
}
