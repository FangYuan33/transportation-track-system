package com.tts.base.dto;

/**
 * 服务器 dto 对象
 *
 * @author FangYuan
 * @since 2022-12-21 20:05:20
 */
public class BaseServerDto {

    /**
     * 服务器ip
     */
    private String serverIp;
    /**
     * 服务器类型: master ,slaver
     */
    private String serverType;
    /**
     * 服务器注册时间
     */
    private String time;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
