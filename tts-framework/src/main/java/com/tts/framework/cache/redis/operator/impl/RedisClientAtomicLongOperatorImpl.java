package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientAtomicLongOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RAtomicLong;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisClientAtomicLongOperatorImpl implements RedisClientAtomicLongOperator {

    private RedisClientConfig redisClientConfig;

    public RedisClientAtomicLongOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    private RAtomicLong init(String namespace, String key) {
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        return RedisClient.getInstance().getRedissonClient().getAtomicLong(realKey);
    }

    @Override
    public Long getAndDecrement(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.getAndDecrement();
    }

    @Override
    public Long addAndGet(String namespace, String key, long delta) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.addAndGet(delta);
    }

    @Override
    public Boolean compareAndSet(String namespace, String key, long expect, long update) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.compareAndSet(expect, update);
    }

    @Override
    public Long decrementAndGet(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.decrementAndGet();
    }

    @Override
    public Long get(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.get();
    }

    @Override
    public Long getAndDelete(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.getAndDelete();
    }

    @Override
    public Long getAndAdd(String namespace, String key, long delta) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.getAndAdd(delta);
    }

    @Override
    public Long getAndSet(String namespace, String key, long newValue) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.getAndSet(newValue);
    }

    @Override
    public Long incrementAndGet(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.incrementAndGet();
    }

    @Override
    public Long getAndIncrement(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.getAndIncrement();
    }

    @Override
    public Boolean set(String namespace, String key, long newValue) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        rAtomicLong.set(newValue);
        return true;
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.remainTimeToLive();
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RAtomicLong rAtomicLong = init(namespace, key);
        return rAtomicLong.addListener(listener);
    }
}
