package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientMapOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RMap;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisClientMapOperatorImpl implements RedisClientMapOperator {


    private RedisClientConfig redisClientConfig;

    public RedisClientMapOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }


    @Override
    public <K, V> V get(String namespace, String key, K hashKey, V type) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.get(hashKey);
    }

    @Override
    public <K, V> V putIfAbsent(String namespace, String key, K hashKey, V value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.putIfAbsent(hashKey, value);
    }

    @Override
    public <K, V> V addAndGet(String namespace, String key, K hashKey, Number delta, V valueType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.addAndGet(hashKey, delta);
    }

    @Override
    public Boolean containsKey(String namespace, String key, Object hashKey) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.containsKey(hashKey);
    }

    @Override
    public Boolean containsValue(String namespace, String key, Object value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.containsValue(value);
    }

    @Override
    public <K, V> V remove(String namespace, String key, K hashKey, V valueType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.remove(hashKey);
    }

    @Override
    public Boolean removeWhenEqValue(String namespace, String key, Object hashKey, Object value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.remove(hashKey, value);
    }

    @Override
    public <K, V> V replace(String namespace, String key, K hashKey, V value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.replace(hashKey, value);
    }

    @Override
    public <K, V> Boolean replace(String namespace, String key, K hashKey, V oldValue, V newValue) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.replace(hashKey, oldValue, newValue);
    }

    @Override
    public <K, V> Boolean putAll(String namespace, String key, Map<? extends K, ? extends V> map) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        rMap.putAll(map);
        return true;
    }

    @Override
    public <K, V> Boolean putAll(String namespace, String key, Map<? extends K, ? extends V> map, int batchSize) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        rMap.putAll(map, batchSize);
        return true;
    }

    @Override
    public <K> Map<K, ?> getAll(String namespace, String key, Set<K> hashKeys) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, ?> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.getAll(hashKeys);
    }

    @Override
    public <K> Long fastRemove(String namespace, String key, K... hashKeys) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, ?> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.fastRemove(hashKeys);
    }

    @Override
    public <K, V> Boolean fastPut(String namespace, String key, K hashKey, V value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.fastPut(hashKey, value);
    }

    @Override
    public <K, V> Boolean fastReplace(String namespace, String key, K hashKey, V value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.fastReplace(hashKey, value);
    }

    @Override
    public <K, V> Boolean fastPutIfAbsent(String namespace, String key, K hashKey, V value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, V> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.fastPutIfAbsent(hashKey, value);
    }

    @Override
    public <K> Set<K> keySet(String namespace, String key, K hashkeyType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, ?> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.keySet();
    }

    @Override
    public <K> Set<K> keySet(String namespace, String key, int count, K hashkeyType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, ?> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.keySet(count);
    }

    @Override
    public <K> Set<K> keySet(String namespace, String key, String pattern, int count, K keyType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, ?> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.keySet(pattern, count);
    }

    @Override
    public <K> Set<K> keySet(String namespace, String key, String pattern, K keyType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap<K, ?> rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.keySet(pattern);
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.remainTimeToLive();
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RMap rMap = RedisClient.getInstance().getRedissonClient().getMap(realKey);
        return rMap.addListener(listener);
    }
}
