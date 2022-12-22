package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientBitSetOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RBitSet;

import java.util.BitSet;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisClientBitSetOperatorImpl implements RedisClientBitSetOperator {

    private RedisClientConfig redisClientConfig;

    public RedisClientBitSetOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    private RBitSet init(String namespace, String key) {
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        return RedisClient.getInstance().getRedissonClient().getBitSet(realKey);
    }

    @Override
    public Boolean set(String namespace, String key, long fromIndex, long toIndex, boolean value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.set(fromIndex, toIndex, value);
        return true;
    }

    @Override
    public Boolean clear(String namespace, String key, long fromIndex, long toIndex) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.clear(fromIndex, toIndex);
        return true;
    }

    @Override
    public Boolean set(String namespace, String key, BitSet bs) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.set(bs);
        return true;
    }

    @Override
    public Boolean not(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.not();
        return true;
    }

    @Override
    public Boolean set(String namespace, String key, long fromIndex, long toIndex) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.set(fromIndex, toIndex);
        return true;
    }

    @Override
    public Boolean get(String namespace, String key, long bitIndex) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.get(bitIndex);
    }

    @Override
    public Boolean set(String namespace, String key, long bitIndex) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.set(bitIndex);
    }

    @Override
    public Boolean set(String namespace, String key, long bitIndex, boolean value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.set(bitIndex, value);
    }

    @Override
    public byte[] toByteArray(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.toByteArray();
    }

    @Override
    public Long cardinality(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.cardinality();
    }

    @Override
    public Boolean clear(String namespace, String key, long bitIndex) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.clear(bitIndex);
        return true;

    }

    @Override
    public Boolean clear(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.clear();
        return true;
    }

    @Override
    public BitSet asBitSet(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.asBitSet();
    }

    @Override
    public Boolean or(String namespace, String key, String... bitSetNames) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.or(bitSetNames);
        return true;
    }

    @Override
    public Boolean and(String namespace, String key, String... bitSetNames) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.and(bitSetNames);
        return true;
    }

    @Override
    public Boolean xor(String namespace, String key, String... bitSetNames) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        rBitSet.xor(bitSetNames);
        return true;
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.remainTimeToLive();
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        RBitSet rBitSet = init(namespace, key);
        return rBitSet.addListener(listener);
    }
}
