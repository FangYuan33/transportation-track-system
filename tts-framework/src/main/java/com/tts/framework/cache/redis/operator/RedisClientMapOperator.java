package com.tts.framework.cache.redis.operator;


import org.redisson.api.ObjectListener;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis  集合 Map  操作接口
 *
 * 基于Redis的Redisson的分布式映射结构的RMap Java对象实现了java.util.concurrent.ConcurrentMap接口和java.util.Map接口。
 * 与HashMap不同的是，RMap保持了元素的插入顺序。该对象的最大容量受Redis限制，最大元素数量是4 294 967 295个。
 *
 * 在特定的场景下，映射缓存（Map）上的高度频繁的读取操作，使网络通信都被视为瓶颈时，可以使用Redisson提供的带有本地缓存功能的映射。
 *
 * This map uses serialized state of key instead of hashCode or equals methods.
 * This map doesn't allow to store <code>null</code> as key or value.
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientMapOperator {

		/**
		 * Returns the value mapped by defined <code>key</code> or {@code null} if value is absent.
		 * <p>
		 * If map doesn't contain value for specified key and {@link MapLoader} is defined
		 * then value will be loaded in read-through mode.
		 *
		 * @param key the key
		 * @return the value mapped by defined <code>key</code> or {@code null} if value is absent
		 */
		<K,V> V get(String namespace,String key,K hashKey,V type);



		/**
		 * Stores the specified <code>value</code> mapped by specified <code>key</code>
		 * only if there is no value with specified<code>key</code> stored before.
		 * <p>
		 * If {@link MapWriter} is defined then new map entry is stored in write-through mode.
		 *
		 * @param key - map key
		 * @param value - map value
		 * @return <code>null</code> if key is a new one in the hash and value was set.
		 *         Previous value if key already exists in the hash and change hasn't been made.
		 */
		<K,V> V putIfAbsent(String namespace,String key, K hashKey, V value);


		/**
		 * Adds the given <code>delta</code> to the current value
		 * by mapped <code>key</code>.
		 *
		 * Works only for <b>numeric</b> values!
		 *
		 * @param key - map key
		 * @param delta the value to add
		 * @return the updated value
		 */
		<K,V> V addAndGet(String namespace,String key, K hashKey, Number delta,V valueType);


		/**
		 * Returns <code>true</code> if this map contains map entry
		 * mapped by specified <code>key</code>, otherwise <code>false</code>
		 *
		 * @param key - map key
		 * @return <code>true</code> if this map contains map entry
		 *          mapped by specified <code>key</code>, otherwise <code>false</code>
		 */
		Boolean containsKey(String namespace,String key, Object hashKey);



		/**
		 * Returns <code>true</code> if this map contains any map entry
		 * with specified <code>value</code>, otherwise <code>false</code>
		 *
		 * @param value - map value
		 * @return <code>true</code> if this map contains any map entry
		 *          with specified <code>value</code>, otherwise <code>false</code>
		 */
		Boolean containsValue(String namespace,String key,Object value);



		/**
		 * Removes map entry by specified <code>key</code> and returns value.
		 * <p>
		 * If {@link MapWriter} is defined then <code>key</code>is deleted in write-through mode.
		 *
		 * @param key - map key
		 * @return deleted value, <code>null</code> if map entry doesn't exist
		 */
		<K,V> V remove(String namespace,String key,K hashKey, V valueType);



		/**
		 * Removes map entry only if it exists with specified <code>key</code> and <code>value</code>.
		 * <p>
		 * If {@link MapWriter} is defined then <code>key</code>is deleted in write-through mode.
		 *
		 * @param key - map key
		 * @param value - map value
		 * @return <code>true</code> if map entry has been removed otherwise <code>false</code>.
		 */
		Boolean removeWhenEqValue(String namespace,String key,Object hashKey, Object value);


		/**
		 * Replaces previous value with a new <code>value</code> mapped by specified <code>key</code>.
		 * Returns <code>null</code> if there is no map entry stored before and doesn't store new map entry.
		 * <p>
		 * If {@link MapWriter} is defined then new <code>value</code>is written in write-through mode.
		 *
		 * @return previous associated value
		 *         or <code>null</code> if there is no map entry stored before and doesn't store new map entry
		 */
		<K,V> V replace(String namespace,String key,K hashKey, V valueType);

		/**
		 * Replaces previous <code>oldValue</code> with a <code>newValue</code> mapped by specified <code>key</code>.
		 * Returns <code>false</code> if previous value doesn't exist or equal to <code>oldValue</code>.
		 * <p>
		 * If {@link MapWriter} is defined then <code>newValue</code>is written in write-through mode.
		 *
		 * @param key - map key
		 * @param oldValue - map old value
		 * @param newValue - map new value
		 * @return <code>true</code> if value has been replaced otherwise <code>false</code>.
		 */
		<K,V> Boolean replace(String namespace,String key,K hashKey, V oldValue, V newValue);

		/**
		 * Stores map entries specified in <code>map</code> object in batch mode.
		 * <p>
		 * If {@link MapWriter} is defined then map entries will be stored in write-through mode.
		 *
		 * @param map mappings to be stored in this map
		 */
		 <K,V>  Boolean putAll(String namespace,String key, Map<? extends K, ? extends V> map);


		/**
		 * Stores map entries specified in <code>map</code> object in batch mode.
		 * Batch inserted by chunks limited by <code>batchSize</code> value
		 * to avoid OOM and/or Redis response timeout error for map with big size.
		 * <p>
		 * If {@link MapWriter} is defined then map entries are stored in write-through mode.
		 *
		 * @param map mappings to be stored in this map
		 * @param batchSize - size of map entries batch
		 */
		<K,V> Boolean putAll(String namespace,String key,Map<? extends K, ? extends V> map, int batchSize);

		/**
		 * Returns map slice contained the mappings with defined <code>keys</code>.
		 * <p>
		 * If map doesn't contain value/values for specified key/keys and {@link MapLoader} is defined
		 * then value/values will be loaded in read-through mode.
		 * <p>
		 * The returned map is <b>NOT</b> backed by the original map.
		 *
		 * @return Map slice
		 */
	<K>	Map<K, ?> getAll(String namespace,String key,Set<K> hashKeys);

		/**
		 * Removes map entries mapped by specified <code>keys</code>.
		 * If {@link MapWriter} is defined then <code>keys</code>are deleted in write-through mode.
		 *
		 * @return the number of keys that were removed from the hash, not including specified but non existing keys
		 */
		<K> Long fastRemove(String namespace,String key,K... hashKeys);

		/**
		 * Stores the specified <code>value</code> mapped by specified <code>key</code>.
		 * Returns <code>true</code> if key is a new key in the hash and value was set or
		 * <code>false</code> if key already exists in the hash and the value was updated.
		 * <p>
		 * If {@link MapWriter} is defined then map entry is stored in write-through mode.
		 *
		 * @param key - map key
		 * @param value - map value
		 * @return <code>true</code> if key is a new key in the hash and value was set.
		 *         <code>false</code> if key already exists in the hash and the value was updated.
		 */
		<K,V> Boolean fastPut(String namespace,String key, K hashKey, V value);

		/**
		 * Replaces previous value with a new <code>value</code> mapped by specified <code>key</code>.
		 * <p>
		 * Works faster than <code>replace</code> but not returning
		 * the previous value.
		 * <p>
		 * Returns <code>true</code> if key exists and value was updated or
		 * <code>false</code> if key doesn't exists and value wasn't updated.
		 * <p>
		 * If {@link MapWriter} is defined then new map entry is stored in write-through mode.
		 *
		 * @param key - map key
		 * @param value - map value
		 * @return <code>true</code> if key exists and value was updated.
		 *         <code>false</code> if key doesn't exists and value wasn't updated.
		 */
		<K,V>  Boolean fastReplace(String namespace,String key,K hashKey, V value);

		/**
		 * Stores the specified <code>value</code> mapped by specified <code>key</code>
		 * only if there is no value with specified<code>key</code> stored before.
		 * <p>
		 * Returns <code>true</code> if key is a new one in the hash and value was set or
		 * <code>false</code> if key already exists in the hash and change hasn't been made.
		 * If {@link MapWriter} is defined then new map entry is stored in write-through mode.
		 *
		 * @param key - map key
		 * @param value - map value
		 * @return <code>true</code> if key is a new one in the hash and value was set.
		 *         <code>false</code> if key already exists in the hash and change hasn't been made.
		 */
		<K,V> Boolean fastPutIfAbsent(String namespace,String key, K hashKey, V value);



		/**
		 * Returns key set of this map.
		 * Keys are loaded in batch. Batch size is <code>10</code>.
		 *
		 *
		 * @return key set
		 */
		<K> Set<K> keySet(String namespace,String key,K hashKeyType);

		/**
		 * Returns key set of this map.
		 * Keys are loaded in batch. Batch size is defined by <code>count</code> param.
		 *
		 *
		 * @param count - size of keys batch
		 * @return key set
		 */
		<K>  Set<K> keySet(String namespace,String key,int count,K hashKeyType);


		/**
		 * Returns key set of this map.
		 * If <code>pattern</code> is not null then only keys match this pattern are loaded.
		 * Keys are loaded in batch. Batch size is defined by <code>count</code> param.
		 * <p>
		 * Use <code>org.redisson.client.codec.StringCodec</code> for Map keys.
		 * <p>
		 *
		 *  Supported glob-style patterns:
		 *  <p>
		 *    h?llo subscribes to hello, hallo and hxllo
		 *    <p>
		 *    h*llo subscribes to hllo and heeeello
		 *    <p>
		 *    h[ae]llo subscribes to hello and hallo, but not hillo
		 *
		 *
		 * @param pattern - key pattern
		 * @param count - size of keys batch
		 * @return key set
		 */
		<K> Set<K> keySet(String namespace,String key,String pattern, int count,K hashKeyType);

		/**
		 * Returns key set of this map.
		 * If <code>pattern</code> is not null then only keys match this pattern are loaded.
		 * <p>
		 * Use <code>org.redisson.client.codec.StringCodec</code> for Map keys.
		 * <p>
		 *
		 *  Supported glob-style patterns:
		 *  <p>
		 *    h?llo subscribes to hello, hallo and hxllo
		 *    <p>
		 *    h*llo subscribes to hllo and heeeello
		 *    <p>
		 *    h[ae]llo subscribes to hello and hallo, but not hillo
		 *
		 *
		 * @param pattern - key pattern
		 * @return key set
		 */
		<K> Set<K> keySet(String namespace,String key,String pattern,K hashKeyType);


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
