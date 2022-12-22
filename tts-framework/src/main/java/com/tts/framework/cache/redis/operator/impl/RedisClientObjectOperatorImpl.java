package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientConstant;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientObjectOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RBucket;
import org.redisson.client.codec.Codec;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisClientObjectOperatorImpl<T> implements RedisClientObjectOperator<T> {

    private RedisClientConfig redisClientConfig;
    private Codec defaultCodec;

    public RedisClientObjectOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    public RedisClientObjectOperatorImpl(RedisClientConfig redisClientConfig, Codec codec) {
        this.redisClientConfig = redisClientConfig;
        this.defaultCodec = codec;
    }

    private RBucket<T> getBucket(String realKey) {
        if (defaultCodec == null) {
            RBucket<T> bucket = RedisClient.getInstance().getRedissonClient().getBucket(realKey);
            return bucket;
        } else {
            RBucket<T> bucket = RedisClient.getInstance().getRedissonClient().getBucket(realKey, defaultCodec);
            return bucket;
        }
    }

    @Override
    public Boolean set(String namespace, String key, T value) {

        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }

        String realKey = RedisClientTools.generateRealKey(namespace, key);

        RBucket<T> bucket = getBucket(realKey);

        bucket.set(value);
        return true;
    }

    public Boolean setWithDefaultTime(String namespace, String key, T value) {
        return set(namespace, key, value, RedisClientConstant.DEFAULT_TIME_OUT_DAY, TimeUnit.DAYS);
    }

    @Override
    public Boolean set(String namespace, String key, T value, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        bucket.set(value, timeToLive, timeUnit);
        return true;
    }

    @Override
    public Boolean setWithDefaultTTL(String namespace, String key, T value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }

        int defaultTTL = redisClientConfig.getDefaultTTL();

        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);

        //如果为 -1 则表示 默认 不设置 有效期
        if (RedisClientConstant.DEFAULT_VALUE == defaultTTL) {
            bucket.set(value);
        } else {
            bucket.set(value, defaultTTL, TimeUnit.MINUTES);
        }

        return true;
    }

    @Override
    public T getAndSet(String namespace, String key, T value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.getAndSet(value);
    }

    @Override
    public T getAndSet(String namespace, String key, T value, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.getAndSet(value, timeToLive, timeUnit);
    }

    @Override
    public Boolean compareAndSet(String namespace, String key, T expect, T update) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.compareAndSet(expect, update);
    }

    @Override
    public Boolean setIfExists(String namespace, String key, T value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.setIfExists(value);
    }

    @Override
    public Boolean setIfExists(String namespace, String key, T value, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.setIfExists(value, timeToLive, timeUnit);
    }

    @Override
    public Boolean trySet(String namespace, String key, T value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.trySet(value);
    }

    @Override
    public Boolean trySet(String namespace, String key, T value, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.trySet(value, timeToLive, timeUnit);
    }

    @Override
    public T get(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = RedisClient.getInstance().getRedissonClient().getBucket(realKey);
        return bucket.get();
    }

    @Override
    public T getAndDelete(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket<T> bucket = getBucket(realKey);
        return bucket.getAndDelete();
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket bucket = getBucket(realKey);
        return bucket.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket bucket = getBucket(realKey);
        return bucket.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket bucket = getBucket(realKey);
        return bucket.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket bucket = getBucket(realKey);
        return bucket.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket bucket = getBucket(realKey);
        return bucket.remainTimeToLive();
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RBucket bucket = getBucket(realKey);
        return bucket.addListener(listener);
    }
}
