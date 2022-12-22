package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientDelayedQueueOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisClientDelayedQueueOperatorImpl implements RedisClientDelayedQueueOperator {

    private RedisClientConfig redisClientConfig;

    public RedisClientDelayedQueueOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    @Override
    public <E> Boolean add(String namespace, String key, E value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<E> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.add(value);
    }


    @Override
    public <E> Boolean offer(String namespace, String key, E value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<E> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.offer(value);
    }

    @Override
    public <E> E remove(String namespace, String key, E valueType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<E> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.remove();
    }

    @Override
    public <E> E poll(String namespace, String key, E valueType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<E> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.poll();
    }

    @Override
    public <E> E element(String namespace, String key, E valueType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<E> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.element();
    }

    @Override
    public <E> E peek(String namespace, String key, E valueType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<E> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<E> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.peek();
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.remainTimeToLive();
    }

    @Override
    public <V> V pollLastAndOfferFirstTo(String namespace, String key, String queueName, V v) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<V> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<V> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.pollLastAndOfferFirstTo(queueName);
    }

    @Override
    public <V> List<V> readAll(String namespace, String key, V v) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<V> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<V> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.readAll();
    }

    @Override
    public <V> List<V> poll(String namespace, String key, int limit, V v) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<V> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<V> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.poll(limit);
    }

    @Override
    public <V> Boolean offer(String namespace, String key, V value, long delay, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue<V> rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue<V> rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        rDelayedQueue.offer(value, delay, timeUnit);
        return true;
    }


    @Override
    public Boolean destroy(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        rDelayedQueue.destroy();
        return true;
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RQueue rQueue = RedisClient.getInstance().getRedissonClient().getQueue(realKey);
        RDelayedQueue rDelayedQueue = RedisClient.getInstance().getRedissonClient().getDelayedQueue(rQueue);
        return rDelayedQueue.addListener(listener);
    }
}
