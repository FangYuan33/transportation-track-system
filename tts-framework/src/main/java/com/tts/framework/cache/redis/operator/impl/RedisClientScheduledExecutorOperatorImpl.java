package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientScheduledExecutorOperator;
import org.redisson.api.RScheduledExecutorService;

public class RedisClientScheduledExecutorOperatorImpl implements RedisClientScheduledExecutorOperator {


    private RedisClientConfig redisClientConfig;

    public RedisClientScheduledExecutorOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }


    @Override
    public RScheduledExecutorService getExecutorService(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        return RedisClient.getInstance().getRedissonClient().getExecutorService(realKey);
    }
}
