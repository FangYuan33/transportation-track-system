package com.tts.base.constant;

/**
 * base 组件 全局常量
 */
public class BaseConstant {

    public final static Integer DEFAULT_VALUE = -1;
    public final static String DEFAULT_SYSTEM_NAME = "默认业务系统";
    public final static String DEFAULT_TENANT_NAME = "默认业务租户";

    // 节点心跳间隔 固定为 10秒
    public final static Long HEARTBEAT_INTERVAL = 10 * 1000L;
}
