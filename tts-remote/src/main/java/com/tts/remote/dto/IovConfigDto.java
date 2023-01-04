package com.tts.remote.dto;

import java.io.Serializable;

public class IovConfigDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 中交兴路、G7
     */
    private String iovType;

    /**
     * 加密后的json字符串格式的iov gps设备配置信息
     */
    private String configInfo;

    public String getIovType() {
        return iovType;
    }

    public String getConfigInfo() {
        return configInfo;
    }

    public void setIovType(String iovType) {
        this.iovType = iovType;
    }

    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo;
    }
}
