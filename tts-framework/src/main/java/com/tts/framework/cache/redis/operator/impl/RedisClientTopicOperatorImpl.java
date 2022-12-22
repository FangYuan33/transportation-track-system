package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientTopicOperator;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.StatusListener;

import java.util.List;

public class RedisClientTopicOperatorImpl implements RedisClientTopicOperator {

    private RedisClientConfig redisClientConfig;

    public RedisClientTopicOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    @Override
    public List<String> getChannelNames(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        return rTopic.getChannelNames();
    }

    @Override
    public Long publish(String namespace, String key, Object message) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        return rTopic.publish(message);
    }

    @Override
    public <M> Integer addListener(String namespace, String key, Class<M> type, MessageListener<? extends M> listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        return rTopic.addListener(type, listener);
    }

    @Override
    public Integer addListener(String namespace, String key, StatusListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        return rTopic.addListener(listener);
    }

    @Override
    public Boolean removeListener(String namespace, String key, MessageListener<?> listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        rTopic.removeListener(listener);
        return true;
    }

    @Override
    public Boolean removeListener(String namespace, String key, Integer... listenerIds) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        rTopic.removeListener(listenerIds);
        return true;
    }

    @Override
    public Boolean removeAllListeners(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        rTopic.removeAllListeners();
        return true;
    }

    @Override
    public Integer countListeners(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        return rTopic.countListeners();
    }

    @Override
    public Long countSubscribers(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RTopic rTopic = RedisClient.getInstance().getRedissonClient().getTopic(realKey);
        return rTopic.countSubscribers();
    }

}
