package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientSetOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RSet;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisClientSetOperatorImpl implements RedisClientSetOperator {

    private RedisClientConfig redisClientConfig;

    public RedisClientSetOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }


    @Override
    public <V> Set<V> readAll(String namespace, String key, V v) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet<V> rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.readAll();
    }

    @Override
    public Integer union(String namespace, String key, String... names) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.union(names);
    }

    @Override
    public <V> Set<V> readUnion(String namespace, String key, V v, String... names) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet<V> rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.readUnion(names);
    }

    @Override
    public Integer diff(String namespace, String key, String... names) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.diff(names);
    }

    @Override
    public <V> Set<V> readDiff(String namespace, String key, V v, String... names) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.readDiff(names);
    }

    @Override
    public Integer intersection(String namespace, String key, String... names) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.intersection(names);
    }

    @Override
    public <V> Set<V> readIntersection(String namespace, String key, V v, String... names) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet<V> rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.readIntersection(names);
    }

    @Override
    public Integer size(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.size();
    }

    @Override
    public Boolean isEmpty(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.isEmpty();
    }

    @Override
    public Boolean contains(String namespace, String key, Object o) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.contains(o);
    }

    @Override
    public <E> Iterator<E> iterator(String namespace, String key, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.iterator();
    }

    @Override
    public Object[] toArray(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.toArray();
    }

    @Override
    public <E> Boolean add(String namespace, String key, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.add(e);
    }

    @Override
    public Boolean remove(String namespace, String key, Object o) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.remove(o);
    }

    @Override
    public Boolean containsAll(String namespace, String key, Collection<?> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.containsAll(c);
    }

    @Override
    public <E> Boolean addAll(String namespace, String key, Collection<? extends E> c, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet<E> rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.addAll(c);
    }

    @Override
    public Boolean retainAll(String namespace, String key, Collection<?> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.retainAll(c);
    }

    @Override
    public Boolean removeAll(String namespace, String key, Collection<?> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.removeAll(c);
    }

    @Override
    public Boolean clear(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        rSet.clear();
        return true;
    }

    @Override
    public <V> V random(String namespace, String key, V vType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet<V> rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.random();
    }

    @Override
    public <V> Set<V> random(String namespace, String key, int count, V vType) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet<V> rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.random(count);
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.remainTimeToLive();
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RSet rSet = RedisClient.getInstance().getRedissonClient().getSet(realKey);
        return rSet.addListener(listener);
    }
}
