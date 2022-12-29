package com.tts;

import com.tts.base.zookeeper.properties.ZkNodeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 启动程序
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TransportationTrackSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransportationTrackSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  TTS系统启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
