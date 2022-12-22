package com.tts.framework.cache.redis.operator;


import org.redisson.api.ObjectListener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis  AtomicLong  操作接口
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientAtomicLongOperator {

		/**
		 * Atomically decrements by one the current value.
		 *
		 * @return the previous value
		 */
		Long getAndDecrement(String namespace,String key);

		/**
		 * Atomically adds the given value to the current value.
		 *
		 * @param delta the value to add
		 * @return the updated value
		 */
		Long addAndGet(String namespace,String key,long delta);

		/**
		 * Atomically sets the value to the given updated value
		 * only if the current value {@code ==} the expected value.
		 *
		 * @param expect the expected value
		 * @param update the new value
		 * @return true if successful; or false if the actual value
		 *         was not equal to the expected value.
		 */
		Boolean compareAndSet(String namespace,String key,long expect, long update);

		/**
		 * Atomically decrements the current value by one.
		 *
		 * @return the updated value
		 */
		Long decrementAndGet(String namespace,String key);

		/**
		 * Returns current value.
		 *
		 * @return the current value
		 */
		Long get(String namespace,String key);

		/**
		 * Gets and deletes object
		 *
		 * @return the current value
		 */
		Long getAndDelete(String namespace,String key);

		/**
		 * Atomically adds the given value to the current value.
		 *
		 * @param delta the value to add
		 * @return the old value before the add
		 */
		Long getAndAdd(String namespace,String key,long delta);

		/**
		 * Atomically sets the given value and returns the old value.
		 *
		 * @param newValue the new value
		 * @return the old value
		 */
		Long getAndSet(String namespace,String key,long newValue);

		/**
		 * Atomically increments the current value by one.
		 *
		 * @return the updated value
		 */
		Long incrementAndGet(String namespace,String key);

		/**
		 * Atomically increments the current value by one.
		 *
		 * @return the old value
		 */
		Long getAndIncrement(String namespace,String key);

		/**
		 * Atomically sets the given value.
		 *
		 * @param newValue the new value
		 */
		Boolean set(String namespace,String key,long newValue);

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
		Boolean expireAt(String namespace, String key, Date timestamp);

		/**
		 * Clear an expire timeout or expire date for object.
		 *
		 * @return <code>true</code> if timeout was removed
		 *         <code>false</code> if object does not exist or does not have an associated timeout
		 */
		Boolean clearExpire(String namespace,String key);

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
		Integer addListener(String namespace,String key,ObjectListener listener);

}
