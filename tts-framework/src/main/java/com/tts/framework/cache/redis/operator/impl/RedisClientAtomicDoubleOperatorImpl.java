package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientAtomicDoubleOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RAtomicDouble;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisClientAtomicDoubleOperatorImpl implements RedisClientAtomicDoubleOperator {


    private RedisClientConfig redisClientConfig;

    public RedisClientAtomicDoubleOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    private RAtomicDouble init(String namespace, String key) {
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        return RedisClient.getInstance().getRedissonClient().getAtomicDouble(realKey);
    }

    @Override
    public Double getAndDecrement(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.getAndDecrement();
    }

    @Override
    public Double addAndGet(String namespace, String key, double delta) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.addAndGet(delta);
    }

    @Override
    public Boolean compareAndSet(String namespace, String key, double expect, double update) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.compareAndSet(expect, update);
    }

    @Override
    public Double decrementAndGet(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.decrementAndGet();
    }

    @Override
    public Double get(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.get();
    }

    @Override
    public Double getAndDelete(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.getAndDelete();
    }

    @Override
    public Double getAndAdd(String namespace, String key, double delta) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.getAndAdd(delta);
    }

    @Override
    public Double getAndSet(String namespace, String key, double newValue) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.getAndSet(newValue);
    }

    @Override
    public Double incrementAndGet(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.incrementAndGet();
    }

    @Override
    public Double getAndIncrement(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.getAndIncrement();
    }

    @Override
    public Boolean set(String namespace, String key, double newValue) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        rAtomicDouble.set(newValue);
        return true;
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.remainTimeToLive();
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicDouble rAtomicDouble = init(namespace, key);
        return rAtomicDouble.addListener(listener);
    }
}
