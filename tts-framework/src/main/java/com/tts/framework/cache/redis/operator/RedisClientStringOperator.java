package com.tts.framework.cache.redis.operator;


import org.redisson.api.ObjectListener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis 字符串对象操作 操作接口
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientStringOperator {

       /**
        * 存储 <nameSpace_key,value> 到 redis 中
        * @param nameSpace
        * @param key
        * @param value
        * @return
        */
       Boolean set(String namespace,String key,String value);

       /**
        * 存储 <nameSpace_key,value> 到 redis 中, 并设置有效期
        * @param nameSpace
        * @param key
        * @param value
        * @param timeToLive
        * @param timeUnit
        * @return
        */
       Boolean set(String namespace,String key,String value,long timeToLive, TimeUnit timeUnit);


        /**
         * 根据设置的 默认时间有效期存储数据
         * @param namespace
         * @param key
         * @param value
         * @param <T>
         * @return
         */
        Boolean setWithDefaultTTL(String namespace, String key, String value);

    /**
        * 先获取 nameSpace_key 当前的值,然后存储其新的值 value 到redis中
        * 这个过程是个原子操作
        * @param nameSpace
        * @param key
        * @param value
        * @return
        */
       String getAndSet(String namespace,String key,String value);


       /**
        * 先获取 nameSpace_key 当前的值,然后存储其新的值 value 到redis中,并设置有效期
        * 这个过程是个原子操作
        * @param nameSpace
        * @param key
        * @param value
        * @param timeToLive
        * @param timeUnit
        * @return
        */
       String getAndSet(String namespace,String key,String value,long timeToLive, TimeUnit timeUnit);


       /**
        * 如果 namespace_key 当前的值 等于 expect 则将其 更新为 update,并且返回true
        * 否则 返回 false
        * 这个过程是原子操作
        * @param namespace
        * @param expect
        * @param update
        * @return
        */
       Boolean compareAndSet(String namespace,String key, String  expect, String update);


       /**
        * 如果 namespace_key 存在则给其设置为 value,并返回true
        * 否则不设置,并返回false
        * 这个过程是原子操作
        * @param namespace
        * @param key
        * @param value
        * @return
        */
       Boolean setIfExists(String namespace,String key,String value);

       /**
        * 如果 namespace_key 存在则给其设置为 value,并返回true,并设置有效期
        * 否则不设置,并返回false
        * 这个过程是原子操作
        * @param namespace
        * @param key
        * @param value
        * @param timeToLive
        * @param timeUnit
        * @return
        */
       Boolean setIfExists(String namespace,String key,String value,long timeToLive, TimeUnit timeUnit);


       /**
        * 如果 namespace_key 不存在,则将其设置为 value,并返回true
        * 否则 不做任何操作并返回 false
        * 这个过程是原子操作
        * @param namespace
        * @param key
        * @param value
        * @return
        */
       Boolean trySet(String namespace,String key,String value);


       /**
        * 如果 namespace_key 不存在,则将其设置为 value,并返回true,并设置有效期
        * 否则 不做任何操作并返回 false
        * 这个过程是原子操作
        * @param namespace
        * @param key
        * @param value
        * @param timeToLive
        * @param timeUnit
        * @return
        */
       Boolean trySet(String namespace,String key,String value,long timeToLive, TimeUnit timeUnit);


       /**
        * 返回存储的 namespace_key 对象
        * @param namespace
        * @param key
        * @return
        */
       String get(String namespace,String key);

       /**
        * 返回存储的 namespace_key 对象,并将其删除
        * 这是个原子操作
        * @param namespace
        * @param key
        * @return
        */
       String getAndDelete(String namespace,String key);


    /**
     * Set a timeout for object. After the timeout has expired,
     * the key will automatically be deleted.
     *
     * @param timeToLive - timeout before object will be deleted
     * @param timeUnit - timeout time unit
     * @return <code>true</code> if the timeout was set and <code>false</code> if not
     */
    Boolean expire(String namespace,String key,long timeToLive, TimeUnit timeUnit);

    /**
     * Set an expire date for object. When expire date comes
     * the key will automatically be deleted.
     *
     * @param timestamp - expire date in milliseconds (Unix timestamp)
     * @return <code>true</code> if the timeout was set and <code>false</code> if not
     */
    Boolean expireAt(String namespace,String key,long timestamp);

    /**
     * Set an expire date for object. When expire date comes
     * the key will automatically be deleted.
     *
     * @param timestamp - expire date
     * @return <code>true</code> if the timeout was set and <code>false</code> if not
     */
    Boolean expireAt(String namespace,String key,Date timestamp);

    /**
     * Clear an expire timeout or expire date for object.
     *
     * @return <code>true</code> if timeout was removed
     *         <code>false</code> if object does not exist or does not have an associated timeout
     */
    Boolean clearExpire(String namespace, String key);

    /**
     * Remaining time to live of Redisson object that has a timeout
     *
     * @return time in milliseconds
     *          -2 if the key does not exist.
     *          -1 if the key exists but has no associated expire.
     */
    Long remainTimeToLive(String namespace,String key);

    /**
     * Adds object event listener
     *
     * @see org.redisson.api.ExpiredObjectListener
     * @see org.redisson.api.DeletedObjectListener
     * @see org.redisson.api.listener.SetObjectListener
     *
     * @param listener - object event listener
     * @return listener id
     */
    Integer addListener(String namespace, String key, ObjectListener listener);
}
