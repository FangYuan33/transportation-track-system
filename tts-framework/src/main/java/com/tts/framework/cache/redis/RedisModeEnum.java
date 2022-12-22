package com.tts.framework.cache.redis;


/**
 * redis 模式
 *
 * @author FangYuan
 * @since 2021-01-15
 */
public enum RedisModeEnum {

    //单节点模式
    SINGLE("single"),

    //哨兵模式
    SENTINEL("sentinel"),

    //主从模式
    MASTERSLAVE("masterSlave"),

    //集群模式
    CLUSTER("cluster");

    private final String value;


    RedisModeEnum(String redisMode) {
        this.value = redisMode;
    }

    public String getValue() {
        return value;
    }
}
