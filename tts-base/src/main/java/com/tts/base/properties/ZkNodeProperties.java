package com.tts.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ZkNodeProperties {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * ip:port
     */
    private String address;

    /**
     * session超时时间
     */
    private Integer sessionTimeout;

    /**
     * 链接超时时间
     */
    private Integer connectTimeout;

    /**
     * 重试次数
     */
    private Integer retryCountInterval;

    /**
     * 选主路径
     */
    private String masterPath;

    /**
     * 节点路径，用于监听该路径上的事件
     */
    private String treePath;
}
