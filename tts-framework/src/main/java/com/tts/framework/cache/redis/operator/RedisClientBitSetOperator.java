package com.tts.framework.cache.redis.operator;


import org.redisson.api.ObjectListener;

import java.util.BitSet;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis  bitset 操作接口
 * 常用于用很小的空间,很高的性能,管理大量用户(订单,或其他实体) 的状态,
 * 但是限定只能有 true,false 两种状态
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientBitSetOperator {



		/**
		 * Set all bits to <code>value</code> from <code>fromIndex</code> (inclusive) to <code>toIndex</code> (exclusive)
		 * @param namespace
		 * @param key
		 * @param fromIndex inclusive
		 * @param toIndex exclusive
		 * @param value true = 1, false = 0
		 *
		 */
		Boolean set(String namespace,String key,long fromIndex, long toIndex, boolean value);

		/**
		 * Set all bits to zero from <code>fromIndex</code> (inclusive) to <code>toIndex</code> (exclusive)
		 *
		 * @param fromIndex inclusive
		 * @param toIndex exclusive
		 *
		 */
		Boolean clear(String namespace,String key,long fromIndex, long toIndex);

		/**
		 * Copy bits state of source BitSet object to this object
		 *
		 * @param bs - BitSet source
		 */
		Boolean set(String namespace,String key,BitSet bs);

		/**
		 * Executes NOT operation over all bits
		 */
		Boolean not(String namespace,String key);

		/**
		 * Set all bits to one from <code>fromIndex</code> (inclusive) to <code>toIndex</code> (exclusive)
		 *
		 * @param fromIndex inclusive
		 * @param toIndex exclusive
		 *
		 */
		Boolean set(String namespace,String key,long fromIndex, long toIndex);


		/**
		 * Returns <code>true</code> if bit set to one and <code>false</code> overwise.
		 *
		 * @param bitIndex - index of bit
		 * @return <code>true</code> if bit set to one and <code>false</code> overwise.
		 */
		Boolean get(String namespace,String key,long bitIndex);

		/**
		 * Set bit to one at specified bitIndex
		 *
		 * @param bitIndex - index of bit
		 * @return <code>true</code> - if previous value was true,
		 * <code>false</code> - if previous value was false
		 */
		Boolean set(String namespace,String key,long bitIndex);

		/**
		 * Set bit to <code>value</code> at specified <code>bitIndex</code>
		 *
		 * @param bitIndex - index of bit
		 * @param value true = 1, false = 0
		 * @return <code>true</code> - if previous value was true,
		 * <code>false</code> - if previous value was false
		 *
		 */
		Boolean set(String namespace,String key,long bitIndex, boolean value);

		byte[] toByteArray(String namespace, String key);

		/**
		 * Returns the number of bits set to one.
		 *
		 * @return number of bits
		 */
		Long cardinality(String namespace,String key);

		/**
		 * Set bit to zero at specified <code>bitIndex</code>
		 *
		 * @param bitIndex - index of bit
		 * @return <code>true</code> - if previous value was true,
		 * <code>false</code> - if previous value was false
		 */
		Boolean clear(String namespace,String key,long bitIndex);

		/**
		 * Set all bits to zero
		 */
		Boolean clear(String namespace,String key);

		BitSet asBitSet(String namespace,String key);

		/**
		 * Executes OR operation over this object and specified bitsets.
		 * Stores result into this object.
		 *
		 * @param bitSetNames - name of stored bitsets
		 */
		Boolean or(String namespace,String key,String... bitSetNames);

		/**
		 * Executes AND operation over this object and specified bitsets.
		 * Stores result into this object.
		 *
		 * @param bitSetNames - name of stored bitsets
		 */
		Boolean and(String namespace,String key,String... bitSetNames);

		/**
		 * Executes XOR operation over this object and specified bitsets.
		 * Stores result into this object.
		 *
		 * @param bitSetNames - name of stored bitsets
		 */
		Boolean xor(String namespace,String key,String... bitSetNames);

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
		Integer addListener(String namespace, String key, ObjectListener listener);

}
