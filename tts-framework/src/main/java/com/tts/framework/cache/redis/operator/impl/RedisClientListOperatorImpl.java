package com.tts.framework.cache.redis.operator.impl;

import com.tts.framework.cache.redis.RedisClient;
import com.tts.framework.cache.redis.RedisClientConfig;
import com.tts.framework.cache.redis.RedisClientTools;
import com.tts.framework.cache.redis.operator.RedisClientListOperator;
import org.redisson.api.ObjectListener;
import org.redisson.api.RList;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisClientListOperatorImpl implements RedisClientListOperator {


    private RedisClientConfig redisClientConfig;

    public RedisClientListOperatorImpl(RedisClientConfig redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    @Override
    public Integer size(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.size();
    }

    @Override
    public Boolean isEmpty(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.isEmpty();
    }

    @Override
    public Boolean contains(String namespace, String key, Object value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.contains(value);
    }

    @Override
    public <E> Iterator<E> iterator(String namespace, String key, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.iterator();
    }

    @Override
    public Object[] toArray(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.toArray();
    }


    @Override
    public <E> Boolean add(String namespace, String key, E value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.add(value);
    }

    @Override
    public Boolean remove(String namespace, String key, Object value) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.remove(value);
    }

    @Override
    public Boolean containsAll(String namespace, String key, Collection<?> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.containsAll(c);
    }

    @Override
    public <E> Boolean addAll(String namespace, String key, Collection<? extends E> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.addAll(c);
    }

    @Override
    public <E> Boolean addAll(String namespace, String key, int index, Collection<? extends E> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.addAll(index, c);
    }

    @Override
    public Boolean removeAll(String namespace, String key, Collection<?> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.removeAll(c);
    }

    @Override
    public Boolean retainAll(String namespace, String key, Collection<?> c) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.retainAll(c);
    }

    @Override
    public Boolean clear(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        rList.clear();
        return true;
    }


    @Override
    public <E> E get(String namespace, String key, int index, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList<E> rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.get(index);
    }

    @Override
    public <E> E set(String namespace, String key, int index, E element) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList<E> rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.set(index, element);
    }

    @Override
    public <E> Boolean add(String namespace, String key, int index, E element) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList<E> rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        rList.add(index, element);
        return true;
    }

    @Override
    public <E> E remove(String namespace, String key, int index, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList<E> rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.remove(index);
    }

    @Override
    public Integer indexOf(String namespace, String key, Object o) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.indexOf(o);
    }

    @Override
    public Integer lastIndexOf(String namespace, String key, Object o) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.lastIndexOf(o);
    }

    @Override
    public <E> ListIterator<E> listIterator(String namespace, String key, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList<E> rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.listIterator();
    }

    @Override
    public <E> ListIterator<E> listIterator(String namespace, String key, int index, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList<E> rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.listIterator(index);
    }

    @Override
    public <E> List<E> subList(String namespace, String key, int fromIndex, int toIndex, E e) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList<E> rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.subList(fromIndex, toIndex);
    }

    @Override
    public Boolean expire(String namespace, String key, long timeToLive, TimeUnit timeUnit) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.expire(timeToLive, timeUnit);
    }

    @Override
    public Boolean expireAt(String namespace, String key, long timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.expireAt(timestamp);
    }

    @Override
    public Boolean expireAt(String namespace, String key, Date timestamp) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.expireAt(timestamp);
    }

    @Override
    public Boolean clearExpire(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.clearExpire();
    }

    @Override
    public Long remainTimeToLive(String namespace, String key) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.remainTimeToLive();
    }

    @Override
    public Integer addListener(String namespace, String key, ObjectListener listener) {
        if (!redisClientConfig.isRedisSwitch()) {
            return null;
        }
        String realKey = RedisClientTools.generateRealKey(namespace, key);
        RList rList = RedisClient.getInstance().getRedissonClient().getList(realKey);
        return rList.addListener(listener);
    }
}
