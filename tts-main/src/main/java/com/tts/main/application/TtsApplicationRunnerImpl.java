package com.tts.main.application;

import com.tts.main.iov.IovNodeBackgroundThreadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TtsApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private IovNodeBackgroundThreadService iovLeaderBackgroundThreadService;

    @Override
    public void run(ApplicationArguments args) {
        iovLeaderBackgroundThreadService.initAndStart();
        log.info("完成 启动 IOV leader  后台线程! ");
    }
}
