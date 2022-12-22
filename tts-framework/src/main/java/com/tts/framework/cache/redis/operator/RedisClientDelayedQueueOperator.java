package com.tts.framework.cache.redis.operator;


import org.redisson.api.ObjectListener;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * redis  DelayedQueue  操作接口
 *
 * 注意: 这个接口还需要经进行大量测试,请谨慎使用.
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientDelayedQueueOperator {

		/**
		 * Inserts the specified element into this queue if it is possible to do so
		 * immediately without violating capacity restrictions, returning
		 * {@code true} upon success and throwing an {@code IllegalStateException}
		 * if no space is currently available.
		 *
		 * @param e the element to add
		 * @return {@code true} (as specified by {@link Collection#add})
		 * @throws IllegalStateException if the element cannot be added at this
		 *         time due to capacity restrictions
		 * @throws ClassCastException if the class of the specified element
		 *         prevents it from being added to this queue
		 * @throws NullPointerException if the specified element is null and
		 *         this queue does not permit null elements
		 * @throws IllegalArgumentException if some property of this element
		 *         prevents it from being added to this queue
		 */
		<E> Boolean add(String namespace,String key,E e);

		/**
		 * Inserts the specified element into this queue if it is possible to do
		 * so immediately without violating capacity restrictions.
		 * When using a capacity-restricted queue, this method is generally
		 * preferable to {@link #add}, which can fail to insert an element only
		 * by throwing an exception.
		 *
		 * @param e the element to add
		 * @return {@code true} if the element was added to this queue, else
		 *         {@code false}
		 * @throws ClassCastException if the class of the specified element
		 *         prevents it from being added to this queue
		 * @throws NullPointerException if the specified element is null and
		 *         this queue does not permit null elements
		 * @throws IllegalArgumentException if some property of this element
		 *         prevents it from being added to this queue
		 */
		<E> Boolean offer(String namespace,String key,E e);

		/**
		 * Retrieves and removes the head of this queue.  This method differs
		 * from {@link #poll poll} only in that it throws an exception if this
		 * queue is empty.
		 *
		 * @return the head of this queue
		 * @throws NoSuchElementException if this queue is empty
		 */
		<E> E remove(String namespace,String key, E valueType);

		/**
		 * Retrieves and removes the head of this queue,
		 * or returns {@code null} if this queue is empty.
		 *
		 * @return the head of this queue, or {@code null} if this queue is empty
		 */
		<E> E poll(String namespace,String key, E valueType);

		/**
		 * Retrieves, but does not remove, the head of this queue.  This method
		 * differs from {@link #peek peek} only in that it throws an exception
		 * if this queue is empty.
		 *
		 * @return the head of this queue
		 * @throws NoSuchElementException if this queue is empty
		 */
		<E> E element(String namespace,String key, E valueType);

		/**
		 * Retrieves, but does not remove, the head of this queue,
		 * or returns {@code null} if this queue is empty.
		 *
		 * @return the head of this queue, or {@code null} if this queue is empty
		 */
		<E> E peek(String namespace,String key, E valueType);


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
		 * Retrieves and removes last available tail element of this queue queue and adds it at the head of <code>queueName</code>.
		 *
		 * @param queueName - names of destination queue
		 * @return the tail of this queue, or {@code null} if the
		 *         specified waiting time elapses before an element is available
		 */
	  <V>	V pollLastAndOfferFirstTo(String namespace,String key,String queueName,V v);

		/**
		 * Returns all queue elements at once
		 *
		 * @return elements
		 */
		<V> List<V> readAll(String namespace,String key,V v);

		/**
		 * Retrieves and removes the head elements of this queue.
		 * Elements amount limited by <code>limit</code> param.
		 *
		 * @return list of head elements
		 */
	 <V>	List<V> poll(String namespace,String key,int limit,V v);


		/**
		 * Inserts element into this queue with
		 * specified transfer delay to destination queue.
		 *
		 * @param e the element to add
		 * @param delay for transition
		 * @param timeUnit for delay
		 */
		<V> Boolean offer(String namespace,String key,V e, long delay, TimeUnit timeUnit);

		/**
		 * Destroys object when it's not necessary anymore.
		 */
		Boolean destroy(String namespace,String key);


		/**
		 * Adds object event listener
		 *
		 * @see org.redisson.api.ExpiredObjectListener
		 * @see org.redisson.api.DeletedObjectListener
		 * @see org.redisson.api.listener.ListAddListener
		 * @see org.redisson.api.listener.ListInsertListener
		 * @see org.redisson.api.listener.ListSetListener
		 * @see org.redisson.api.listener.ListRemoveListener
		 * @see org.redisson.api.listener.ListTrimListener
		 *
		 * @param listener - object event listener
		 * @return listener id
		 */
		Integer addListener(String namespace,String key,ObjectListener listener);

}
