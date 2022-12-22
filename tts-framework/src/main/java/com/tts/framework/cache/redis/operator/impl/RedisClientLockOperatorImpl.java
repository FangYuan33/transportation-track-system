package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientLockOperator;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public class RedisClientLockOperatorImpl implements RedisClientLockOperator {

    private RedisClientConfig redisClientConfig;

    public RedisClientLockOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    private RLock init(String namespace, String key) {
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        return RedisClient.getInstance().getRedissonClient().getLock(realKey);
    }

    @Override
    public String getName(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.getName();
    }

    @Override
    public Boolean lockInterruptibly(String namespace, String key, long leaseTime, TimeUnit unit) throws InterruptedException {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        rLock.lockInterruptibly(leaseTime, unit);
        return true;
    }

    @Override
    public Boolean tryLock(String namespace, String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.tryLock(waitTime, leaseTime, unit);
    }

    @Override
    public Boolean lock(String namespace, String key, long leaseTime, TimeUnit unit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        rLock.lock(leaseTime, unit);
        return true;
    }

    @Override
    public Boolean forceUnlock(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        rLock.forceUnlock();
        return true;
    }

    @Override
    public Boolean isLocked(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.isLocked();
    }

    @Override
    public Boolean isHeldByThread(String namespace, String key, long threadId) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.isHeldByThread(threadId);
    }

    @Override
    public Boolean isHeldByCurrentThread(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.isHeldByCurrentThread();
    }

    @Override
    public Integer getHoldCount(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.getHoldCount();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.remainTimeToLive();
    }

    @Override
    public Boolean lock(String namespace, String key) {
        RLock rLock = init(namespace, key);
        rLock.lock();
        return true;
    }

    @Override
    public Boolean lockInterruptibly(String namespace, String key) throws InterruptedException {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        rLock.lockInterruptibly();
        return true;
    }

    @Override
    public Boolean tryLock(String namespace, String key) {
        RLock rLock = init(namespace, key);
        return rLock.tryLock();
    }

    @Override
    public Boolean tryLock(String namespace, String key, long time, TimeUnit unit) throws InterruptedException {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        return rLock.tryLock(time, unit);
    }

    @Override
    public Boolean unlock(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RLock rLock = init(namespace, key);
        rLock.unlock();
        return true;
    }


}
