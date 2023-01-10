package com.tts.runner;

import com.tts.iov.service.IovConfigService;
import com.tts.iov.service.IovTaskRunnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * TTS应用功能服务
 *
 * @author FangYuan
 * @since 2022-12-29 20:57:46
 */
@Slf4j
@Component
public class TtsNodeTaskRunner implements ApplicationRunner {

    @Autowired
    private IovConfigService iovConfigService;
    @Autowired
    private IovTaskRunnerService iovTaskRunnerService;

    @Override
    public void run(ApplicationArguments args) {
        // 解密iov配置信息
        iovConfigService.initialIovConfig();
        // 启动IOV任务服务
        iovTaskRunnerService.start();
    }
}
