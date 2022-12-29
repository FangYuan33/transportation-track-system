package com.tts.runner;

import com.tts.base.zookeeper.TtsZkNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * TTS节点启动类，随系统启动
 *
 * @author wangyilong13
 * @since 2022-12-29 15:13:48
 */
@Slf4j
@Component
public class TtsNodeRunner implements ApplicationRunner {

    @Autowired
    private TtsZkNode ttsZkNode;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("TTS Node Runner start!!!");
        ttsZkNode.start();
        log.info("TTS Node Runner start over!!!");
    }
}
