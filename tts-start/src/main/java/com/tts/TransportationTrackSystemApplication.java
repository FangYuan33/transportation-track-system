package com.tts;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 */
@EnableDubbo(scanBasePackages = "com.tts.iov.remote.impl")
@MapperScan(basePackages = {"com.tts.base.dao", "com.tts.iov.dao"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TransportationTrackSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransportationTrackSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  TTS系统启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
