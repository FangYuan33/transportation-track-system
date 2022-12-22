package com.tts.framework.cache.redis.operator;


import org.redisson.api.ObjectListener;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis  集合Set  操作接口
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientSetOperator {

		/**
		 * Read all elements at once
		 *
		 * @return values
		 */
		<V> Set<V> readAll(String namespace,String key,V v);

		/**
		 * Union sets specified by name and write to current set.
		 * If current set already exists, it is overwritten.
		 *
		 * @param names - name of sets
		 * @return size of union
		 */
		Integer union(String namespace,String key,String... names);

		/**
		 * Union sets specified by name with current set
		 * without current set state change.
		 *
		 * @param names - name of sets
		 * @return values
		 */
		<V> Set<V> readUnion(String namespace,String key,V v,String... names);

		/**
		 * Diff sets specified by name and write to current set.
		 * If current set already exists, it is overwritten.
		 *
		 * @param names - name of sets
		 * @return values
		 */
		Integer diff(String namespace,String key,String... names);

		/**
		 * Diff sets specified by name with current set.
		 * Without current set state change.
		 *
		 * @param names - name of sets
		 * @return values
		 */

		<V> Set<V> readDiff(String namespace,String key,V v,String... names);
		/**
		 * Intersection sets specified by name and write to current set.
		 * If current set already exists, it is overwritten.
		 *
		 * @param names - name of sets
		 * @return size of intersection
		 */
		Integer intersection(String namespace,String key,String... names);

		/**
		 * Intersection sets specified by name with current set
		 * without current set state change.
		 *
		 * @param names - name of sets
		 * @return values
		 */
		<V> Set<V> readIntersection(String namespace,String key,V v,String... names);


		/**
		 * Returns the number of elements in this set (its cardinality).  If this
		 * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
		 * <tt>Integer.MAX_VALUE</tt>.
		 *
		 * @return the number of elements in this set (its cardinality)
		 */
		Integer size(String namespace,String key);

		/**
		 * Returns <tt>true</tt> if this set contains no elements.
		 *
		 * @return <tt>true</tt> if this set contains no elements
		 */
		Boolean isEmpty(String namespace,String key);

		/**
		 * Returns <tt>true</tt> if this set contains the specified element.
		 * More formally, returns <tt>true</tt> if and only if this set
		 * contains an element <tt>e</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
		 *
		 * @param o element whose presence in this set is to be tested
		 * @return <tt>true</tt> if this set contains the specified element
		 * @throws ClassCastException if the type of the specified element
		 *         is incompatible with this set
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if the specified element is null and this
		 *         set does not permit null elements
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 */
		Boolean contains(String namespace,String key,Object o);

		/**
		 * Returns an iterator over the elements in this set.  The elements are
		 * returned in no particular order (unless this set is an instance of some
		 * class that provides a guarantee).
		 *
		 * @return an iterator over the elements in this set
		 */
		<E> Iterator<E> iterator(String namespace,String key,E e);

		/**
		 * Returns an array containing all of the elements in this set.
		 * If this set makes any guarantees as to what order its elements
		 * are returned by its iterator, this method must return the
		 * elements in the same order.
		 *
		 * <p>The returned array will be "safe" in that no references to it
		 * are maintained by this set.  (In other words, this method must
		 * allocate a new array even if this set is backed by an array).
		 * The caller is thus free to modify the returned array.
		 *
		 * <p>This method acts as bridge between array-based and collection-based
		 * APIs.
		 *
		 * @return an array containing all the elements in this set
		 */
		Object[] toArray(String namespace,String key);



		// Modification Operations

		/**
		 * Adds the specified element to this set if it is not already present
		 * (optional operation).  More formally, adds the specified element
		 * <tt>e</tt> to this set if the set contains no element <tt>e2</tt>
		 * such that
		 * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
		 * If this set already contains the element, the call leaves the set
		 * unchanged and returns <tt>false</tt>.  In combination with the
		 * restriction on constructors, this ensures that sets never contain
		 * duplicate elements.
		 *
		 * <p>The stipulation above does not imply that sets must accept all
		 * elements; sets may refuse to add any particular element, including
		 * <tt>null</tt>, and throw an exception, as described in the
		 * specification for {@link Collection#add Collection.add}.
		 * Individual set implementations should clearly document any
		 * restrictions on the elements that they may contain.
		 *
		 * @param e element to be added to this set
		 * @return <tt>true</tt> if this set did not already contain the specified
		 *         element
		 * @throws UnsupportedOperationException if the <tt>add</tt> operation
		 *         is not supported by this set
		 * @throws ClassCastException if the class of the specified element
		 *         prevents it from being added to this set
		 * @throws NullPointerException if the specified element is null and this
		 *         set does not permit null elements
		 * @throws IllegalArgumentException if some property of the specified element
		 *         prevents it from being added to this set
		 */
		<E> Boolean add(String namespace,String key,E e);


		/**
		 * Removes the specified element from this set if it is present
		 * (optional operation).  More formally, removes an element <tt>e</tt>
		 * such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
		 * this set contains such an element.  Returns <tt>true</tt> if this set
		 * contained the element (or equivalently, if this set changed as a
		 * result of the call).  (This set will not contain the element once the
		 * call returns.)
		 *
		 * @param o object to be removed from this set, if present
		 * @return <tt>true</tt> if this set contained the specified element
		 * @throws ClassCastException if the type of the specified element
		 *         is incompatible with this set
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if the specified element is null and this
		 *         set does not permit null elements
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws UnsupportedOperationException if the <tt>remove</tt> operation
		 *         is not supported by this set
		 */
		Boolean remove(String namespace,String key,Object o);


		// Bulk Operations

		Boolean containsAll(String namespace,String key,Collection<?> c);

		/**
		 * Adds all of the elements in the specified collection to this set if
		 * they're not already present (optional operation).  If the specified
		 * collection is also a set, the <tt>addAll</tt> operation effectively
		 * modifies this set so that its value is the <i>union</i> of the two
		 * sets.  The behavior of this operation is undefined if the specified
		 * collection is modified while the operation is in progress.
		 *
		 * @param  c collection containing elements to be added to this set
		 * @return <tt>true</tt> if this set changed as a result of the call
		 *
		 * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
		 *         is not supported by this set
		 * @throws ClassCastException if the class of an element of the
		 *         specified collection prevents it from being added to this set
		 * @throws NullPointerException if the specified collection contains one
		 *         or more null elements and this set does not permit null
		 *         elements, or if the specified collection is null
		 * @throws IllegalArgumentException if some property of an element of the
		 *         specified collection prevents it from being added to this set
		 */
	 <E>	Boolean addAll(String namespace,String key,Collection<? extends E> c,E e);

		/**
		 * Retains only the elements in this set that are contained in the
		 * specified collection (optional operation).  In other words, removes
		 * from this set all of its elements that are not contained in the
		 * specified collection.  If the specified collection is also a set, this
		 * operation effectively modifies this set so that its value is the
		 * <i>intersection</i> of the two sets.
		 *
		 * @param  c collection containing elements to be retained in this set
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
		 *         is not supported by this set
		 * @throws ClassCastException if the class of an element of this set
		 *         is incompatible with the specified collection
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if this set contains a null element and the
		 *         specified collection does not permit null elements
		 *         (<a href="Collection.html#optional-restrictions">optional</a>),
		 *         or if the specified collection is null
		 */
		Boolean retainAll(String namespace,String key,Collection<?> c);

		/**
		 * Removes from this set all of its elements that are contained in the
		 * specified collection (optional operation).  If the specified
		 * collection is also a set, this operation effectively modifies this
		 * set so that its value is the <i>asymmetric set difference</i> of
		 * the two sets.
		 *
		 * @param  c collection containing elements to be removed from this set
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @throws UnsupportedOperationException if the <tt>removeAll</tt> operation
		 *         is not supported by this set
		 * @throws ClassCastException if the class of an element of this set
		 *         is incompatible with the specified collection
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if this set contains a null element and the
		 *         specified collection does not permit null elements
		 *         (<a href="Collection.html#optional-restrictions">optional</a>),
		 *         or if the specified collection is null
		 */
		Boolean removeAll(String namespace,String key,Collection<?> c);

		/**
		 * Removes all of the elements from this set (optional operation).
		 * The set will be empty after this call returns.
		 *
		 * @throws UnsupportedOperationException if the <tt>clear</tt> method
		 *         is not supported by this set
		 */
		Boolean clear(String namespace,String key);


		// Comparison and hashing




		/**
		 * Returns random element
		 *
		 * @return random element
		 */
	<V>	V random(String namespace,String key,V vType);


		/**
		 * Returns random elements from set limited by <code>count</code>
		 *
		 * @param count - values amount to return
		 * @return random elements
		 */
   <V> Set<V> random(String namespace,String key,int count,V vType);

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