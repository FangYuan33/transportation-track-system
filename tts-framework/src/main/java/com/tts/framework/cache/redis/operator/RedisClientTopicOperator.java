package com.tts.framework.cache.redis.operator;

import org.redisson.api.listener.MessageListener;
import org.redisson.api.listener.StatusListener;

import java.util.List;

/**
 * redis  topic  操作接口, 主要用于 订阅消息
 *
 * Distributed topic. Messages are delivered to all message listeners across Redis cluster.
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientTopicOperator {

		/**
		 * Get topic channel names
		 *
		 * @return channel names
		 */
		List<String> getChannelNames(String namespace,String key);

		/**
		 * Publish the message to all subscribers of this topic
		 *
		 * @param message to send
		 * @return the number of clients that received the message
		 */
		Long publish(String namespace,String key,Object message);

		/**
		 * Subscribes to this topic.
		 * <code>MessageListener.onMessage</code> is called when any message
		 * is published on this topic.
		 *
		 * @param <M> - type of message
		 * @param type - type of message
		 * @param listener for messages
		 * @return locally unique listener id
		 * @see MessageListener
		 */
		<M> Integer addListener(String namespace,String key,Class<M> type, MessageListener<? extends M> listener);

		/**
		 * Subscribes to status changes of this topic
		 *
		 * @param listener for messages
		 * @return listener id
		 * @see StatusListener
		 */
		Integer addListener(String namespace,String key,StatusListener listener);

		/**
		 * Removes the listener by its instance
		 *
		 * @param listener - listener instance
		 */
		Boolean removeListener(String namespace,String key,MessageListener<?> listener);

		/**
		 * Removes the listener by <code>id</code> for listening this topic
		 *
		 * @param listenerIds - listener ids
		 */
		Boolean removeListener(String namespace,String key,Integer... listenerIds);

		/**
		 * Removes all listeners from this topic
		 */
		Boolean removeAllListeners(String namespace,String key);

		/**
		 * Returns amount of registered listeners to this topic
		 *
		 * @return amount of listeners
		 */
		Integer countListeners(String namespace,String key);

		/**
		 * Returns amount of subscribers to this topic across all Redisson instances.
		 * Each subscriber may have multiple listeners.
		 *
		 * @return amount of subscribers
		 */
		Long countSubscribers(String namespace,String key);

}
