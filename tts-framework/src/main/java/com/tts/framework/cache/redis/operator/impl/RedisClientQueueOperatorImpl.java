package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientQueueOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RQueue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisClientQueueOperatorImpl implements RedisClientQueueOperator {

    private RedisClientConfig redisClientConfig;

    public RedisClientQueueOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }


    @Override
    public <E> Boolean add(String namespace, String key, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.add(e);
    }

    @Override
    public <E> Boolean offer(String namespace, String key, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.offer(e);
    }

    @Override
    public <E> E remove(String namespace, String key, E eType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.remove();
    }

    @Override
    public <E> E poll(String namespace, String key, E eType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.poll();
    }

    @Override
    public <E> E element(String namespace, String key, E eType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.element();
    }

    @Override
    public <E> E peek(String namespace, String key, E eType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.peek();
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.remainTimeToLive();
    }

    @Override
    public <V> V pollLastAndOfferFirstTo(String namespace, String key, String queueName, V v) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<V> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.pollLastAndOfferFirstTo(queueName);
    }

    @Override
    public <V> List<V> readAll(String namespace, String key, V v) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<V> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.readAll();
    }

    @Override
    public <V> List<V> poll(String namespace, String key, int limit, V v) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<V> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.poll(limit);
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        return rQueue.addListener(listener);
    }


    @Override
    public boolean clear(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return true;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        rQueue.clear();

        return true;
    }

}
