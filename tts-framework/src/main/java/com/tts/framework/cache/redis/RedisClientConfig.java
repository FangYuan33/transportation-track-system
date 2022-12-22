package com.tts.framework.cache.redis;


import org.redisson.config.Config;

/**
 * redis 客户端 缓存配置
 *
 * @author FangYuan
 * @since 2021-01-15
 */
public class RedisClientConfig   {

    /**
     * 开关,是否开启redisclient
     */
    private boolean redisSwitch = false;

    /**
     *  Redis Cluster Mode
     *
     *  single -> single redis server
     *  sentinel -> master-slaves servers
     *  cluster -> cluster servers (数据库配置无效，使用 database = 0）
     *  masterslave
     * */
    private String redisMode;


    /**
     * 是否成功加载配置文件
     * 在完全成功加载并解析配置文件之后设置为true
     */
    private boolean success = false;

    /**
     * 默认消息的有效期,单位是分钟.
     * 当前为默认 为 一周 的时间
     * 如果设置为-1则表示没有有效期
     */
    private int defaultTTL = -1;


    /**
     * redisson 使用的配置信息
     */
    private Config redissonConfig;


    public boolean isRedisSwitch() {
        return redisSwitch;
    }

    public void setRedisSwitch(boolean redisSwitch) {
        this.redisSwitch = redisSwitch;
    }

    public String getRedisMode() {
        return redisMode;
    }

    public void setRedisMode(String redisMode) {
        this.redisMode = redisMode;
    }

    public Config getRedissonConfig() {
        return redissonConfig;
    }

    public void setRedissonConfig(Config redissonConfig) {
        this.redissonConfig = redissonConfig;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getDefaultTTL() {
        return defaultTTL;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setDefaultTTL(int defaultTTL) {
        this.defaultTTL = defaultTTL;
    }
}
