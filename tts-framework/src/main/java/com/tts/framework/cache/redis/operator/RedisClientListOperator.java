package com.tts.framework.cache.redis.operator;

import org.redisson.api.ObjectListener;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis  RList  操作接口
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientListOperator {
		// Query Operations

		/**
		 * Returns the number of elements in this list.  If this list contains
		 * more than <tt>Integer.MAX_VALUE</tt> elements, returns
		 * <tt>Integer.MAX_VALUE</tt>.
		 *
		 * @return the number of elements in this list
		 */
		Integer size(String namespace,String key);

		/**
		 * Returns <tt>true</tt> if this list contains no elements.
		 *
		 * @return <tt>true</tt> if this list contains no elements
		 */
		Boolean isEmpty(String namespace,String key);

		/**
		 * Returns <tt>true</tt> if this list contains the specified element.
		 * More formally, returns <tt>true</tt> if and only if this list contains
		 * at least one element <tt>e</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
		 *
		 * @param o element whose presence in this list is to be tested
		 * @return <tt>true</tt> if this list contains the specified element
		 * @throws ClassCastException if the type of the specified element
		 *         is incompatible with this list
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if the specified element is null and this
		 *         list does not permit null elements
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 */
		Boolean contains(String namespace,String key,Object o);

		/**
		 * Returns an iterator over the elements in this list in proper sequence.
		 *
		 * @return an iterator over the elements in this list in proper sequence
		 */
		<E> Iterator<E> iterator(String namespace,String key,E e);

		/**
		 * Returns an array containing all of the elements in this list in proper
		 * sequence (from first to last element).
		 *
		 * <p>The returned array will be "safe" in that no references to it are
		 * maintained by this list.  (In other words, this method must
		 * allocate a new array even if this list is backed by an array).
		 * The caller is thus free to modify the returned array.
		 *
		 * <p>This method acts as bridge between array-based and collection-based
		 * APIs.
		 *
		 * @return an array containing all of the elements in this list in proper
		 *         sequence
		 * @see Arrays#asList(Object[])
		 */
		Object[] toArray(String namespace,String key);



		// Modification Operations

		/**
		 * Appends the specified element to the end of this list (optional
		 * operation).
		 *
		 * <p>Lists that support this operation may place limitations on what
		 * elements may be added to this list.  In particular, some
		 * lists will refuse to add null elements, and others will impose
		 * restrictions on the type of elements that may be added.  List
		 * classes should clearly specify in their documentation any restrictions
		 * on what elements may be added.
		 *
		 * @param e element to be appended to this list
		 * @return <tt>true</tt> (as specified by {@link Collection#add})
		 * @throws UnsupportedOperationException if the <tt>add</tt> operation
		 *         is not supported by this list
		 * @throws ClassCastException if the class of the specified element
		 *         prevents it from being added to this list
		 * @throws NullPointerException if the specified element is null and this
		 *         list does not permit null elements
		 * @throws IllegalArgumentException if some property of this element
		 *         prevents it from being added to this list
		 */
	 <E>	Boolean add(String namespace,String key,E e);

		/**
		 * Removes the first occurrence of the specified element from this list,
		 * if it is present (optional operation).  If this list does not contain
		 * the element, it is unchanged.  More formally, removes the element with
		 * the lowest index <tt>i</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
		 * (if such an element exists).  Returns <tt>true</tt> if this list
		 * contained the specified element (or equivalently, if this list changed
		 * as a result of the call).
		 *
		 * @param o element to be removed from this list, if present
		 * @return <tt>true</tt> if this list contained the specified element
		 * @throws ClassCastException if the type of the specified element
		 *         is incompatible with this list
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if the specified element is null and this
		 *         list does not permit null elements
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws UnsupportedOperationException if the <tt>remove</tt> operation
		 *         is not supported by this list
		 */
		Boolean remove(String namespace,String key,Object o);


		// Bulk Modification Operations

		/**
		 * Returns <tt>true</tt> if this list contains all of the elements of the
		 * specified collection.
		 *
		 * @param  c collection to be checked for containment in this list
		 * @return <tt>true</tt> if this list contains all of the elements of the
		 *         specified collection
		 * @throws ClassCastException if the types of one or more elements
		 *         in the specified collection are incompatible with this
		 *         list
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if the specified collection contains one
		 *         or more null elements and this list does not permit null
		 *         elements
		 *         (<a href="Collection.html#optional-restrictions">optional</a>),
		 *         or if the specified collection is null
		 */
		Boolean containsAll(String namespace,String key,Collection<?> c);

		/**
		 * Appends all of the elements in the specified collection to the end of
		 * this list, in the order that they are returned by the specified
		 * collection's iterator (optional operation).  The behavior of this
		 * operation is undefined if the specified collection is modified while
		 * the operation is in progress.  (Note that this will occur if the
		 * specified collection is this list, and it's nonempty.)
		 *
		 * @param c collection containing elements to be added to this list
		 * @return <tt>true</tt> if this list changed as a result of the call
		 * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
		 *         is not supported by this list
		 * @throws ClassCastException if the class of an element of the specified
		 *         collection prevents it from being added to this list
		 * @throws NullPointerException if the specified collection contains one
		 *         or more null elements and this list does not permit null
		 *         elements, or if the specified collection is null
		 * @throws IllegalArgumentException if some property of an element of the
		 *         specified collection prevents it from being added to this list
		 */
		<E> Boolean addAll(String namespace,String key,Collection<? extends E> c);

		/**
		 * Inserts all of the elements in the specified collection into this
		 * list at the specified position (optional operation).  Shifts the
		 * element currently at that position (if any) and any subsequent
		 * elements to the right (increases their indices).  The new elements
		 * will appear in this list in the order that they are returned by the
		 * specified collection's iterator.  The behavior of this operation is
		 * undefined if the specified collection is modified while the
		 * operation is in progress.  (Note that this will occur if the specified
		 * collection is this list, and it's nonempty.)
		 *
		 * @param index index at which to insert the first element from the
		 *              specified collection
		 * @param c collection containing elements to be added to this list
		 * @return <tt>true</tt> if this list changed as a result of the call
		 * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
		 *         is not supported by this list
		 * @throws ClassCastException if the class of an element of the specified
		 *         collection prevents it from being added to this list
		 * @throws NullPointerException if the specified collection contains one
		 *         or more null elements and this list does not permit null
		 *         elements, or if the specified collection is null
		 * @throws IllegalArgumentException if some property of an element of the
		 *         specified collection prevents it from being added to this list
		 * @throws IndexOutOfBoundsException if the index is out of range
		 *         (<tt>index &lt; 0 || index &gt; size()</tt>)
		 */
		<E> Boolean addAll(String namespace,String key,int index, Collection<? extends E> c);

		/**
		 * Removes from this list all of its elements that are contained in the
		 * specified collection (optional operation).
		 *
		 * @param c collection containing elements to be removed from this list
		 * @return <tt>true</tt> if this list changed as a result of the call
		 * @throws UnsupportedOperationException if the <tt>removeAll</tt> operation
		 *         is not supported by this list
		 * @throws ClassCastException if the class of an element of this list
		 *         is incompatible with the specified collection
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if this list contains a null element and the
		 *         specified collection does not permit null elements
		 *         (<a href="Collection.html#optional-restrictions">optional</a>),
		 *         or if the specified collection is null
		 */
		Boolean removeAll(String namespace,String key,Collection<?> c);

		/**
		 * Retains only the elements in this list that are contained in the
		 * specified collection (optional operation).  In other words, removes
		 * from this list all of its elements that are not contained in the
		 * specified collection.
		 *
		 * @param c collection containing elements to be retained in this list
		 * @return <tt>true</tt> if this list changed as a result of the call
		 * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
		 *         is not supported by this list
		 * @throws ClassCastException if the class of an element of this list
		 *         is incompatible with the specified collection
		 * (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if this list contains a null element and the
		 *         specified collection does not permit null elements
		 *         (<a href="Collection.html#optional-restrictions">optional</a>),
		 *         or if the specified collection is null
		 */
		Boolean retainAll(String namespace,String key,Collection<?> c);


		/**
		 * Removes all of the elements from this list (optional operation).
		 * The list will be empty after this call returns.
		 *
		 * @throws UnsupportedOperationException if the <tt>clear</tt> operation
		 *         is not supported by this list
		 */
		Boolean clear(String namespace,String key);




		// Positional Access Operations

		/**
		 * Returns the element at the specified position in this list.
		 *
		 * @param index index of the element to return
		 * @return the element at the specified position in this list
		 * @throws IndexOutOfBoundsException if the index is out of range
		 *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
		 */
	 <E>	E get(String namespace,String key,int index,E e);

		/**
		 * Replaces the element at the specified position in this list with the
		 * specified element (optional operation).
		 *
		 * @param index index of the element to replace
		 * @param element element to be stored at the specified position
		 * @return the element previously at the specified position
		 * @throws UnsupportedOperationException if the <tt>set</tt> operation
		 *         is not supported by this list
		 * @throws ClassCastException if the class of the specified element
		 *         prevents it from being added to this list
		 * @throws NullPointerException if the specified element is null and
		 *         this list does not permit null elements
		 * @throws IllegalArgumentException if some property of the specified
		 *         element prevents it from being added to this list
		 * @throws IndexOutOfBoundsException if the index is out of range
		 *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
		 */
	 <E>	E set(String namespace,String key,int index, E element);

		/**
		 * Inserts the specified element at the specified position in this list
		 * (optional operation).  Shifts the element currently at that position
		 * (if any) and any subsequent elements to the right (adds one to their
		 * indices).
		 *
		 * @param index index at which the specified element is to be inserted
		 * @param element element to be inserted
		 * @throws UnsupportedOperationException if the <tt>add</tt> operation
		 *         is not supported by this list
		 * @throws ClassCastException if the class of the specified element
		 *         prevents it from being added to this list
		 * @throws NullPointerException if the specified element is null and
		 *         this list does not permit null elements
		 * @throws IllegalArgumentException if some property of the specified
		 *         element prevents it from being added to this list
		 * @throws IndexOutOfBoundsException if the index is out of range
		 *         (<tt>index &lt; 0 || index &gt; size()</tt>)
		 */
		<E>	Boolean add(String namespace,String key,int index, E element);

		/**
		 * Removes the element at the specified position in this list (optional
		 * operation).  Shifts any subsequent elements to the left (subtracts one
		 * from their indices).  Returns the element that was removed from the
		 * list.
		 *
		 * @param index the index of the element to be removed
		 * @return the element previously at the specified position
		 * @throws UnsupportedOperationException if the <tt>remove</tt> operation
		 *         is not supported by this list
		 * @throws IndexOutOfBoundsException if the index is out of range
		 *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
		 */
	 <E>	E remove(String namespace,String key,int index,E e);


		// Search Operations

		/**
		 * Returns the index of the first occurrence of the specified element
		 * in this list, or -1 if this list does not contain the element.
		 * More formally, returns the lowest index <tt>i</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
		 * or -1 if there is no such index.
		 *
		 * @param o element to search for
		 * @return the index of the first occurrence of the specified element in
		 *         this list, or -1 if this list does not contain the element
		 * @throws ClassCastException if the type of the specified element
		 *         is incompatible with this list
		 *         (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if the specified element is null and this
		 *         list does not permit null elements
		 *         (<a href="Collection.html#optional-restrictions">optional</a>)
		 */
	  Integer indexOf(String namespace,String key,Object o);

		/**
		 * Returns the index of the last occurrence of the specified element
		 * in this list, or -1 if this list does not contain the element.
		 * More formally, returns the highest index <tt>i</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
		 * or -1 if there is no such index.
		 *
		 * @param o element to search for
		 * @return the index of the last occurrence of the specified element in
		 *         this list, or -1 if this list does not contain the element
		 * @throws ClassCastException if the type of the specified element
		 *         is incompatible with this list
		 *         (<a href="Collection.html#optional-restrictions">optional</a>)
		 * @throws NullPointerException if the specified element is null and this
		 *         list does not permit null elements
		 *         (<a href="Collection.html#optional-restrictions">optional</a>)
		 */
		Integer lastIndexOf(String namespace,String key,Object o);


		// List Iterators

		/**
		 * Returns a list iterator over the elements in this list (in proper
		 * sequence).
		 *
		 * @return a list iterator over the elements in this list (in proper
		 *         sequence)
		 */
		<E> ListIterator<E> listIterator(String namespace,String key,E e);

		/**
		 * Returns a list iterator over the elements in this list (in proper
		 * sequence), starting at the specified position in the list.
		 * The specified index indicates the first element that would be
		 * returned by an initial call to {@link ListIterator#next next}.
		 * An initial call to {@link ListIterator#previous previous} would
		 * return the element with the specified index minus one.
		 *
		 * @param index index of the first element to be returned from the
		 *        list iterator (by a call to {@link ListIterator#next next})
		 * @return a list iterator over the elements in this list (in proper
		 *         sequence), starting at the specified position in the list
		 * @throws IndexOutOfBoundsException if the index is out of range
		 *         ({@code index < 0 || index > size()})
		 */
		<E> ListIterator<E> listIterator(String namespace,String key,int index,E e);

		// View

		/**
		 * Returns a view of the portion of this list between the specified
		 * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.  (If
		 * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
		 * empty.)  The returned list is backed by this list, so non-structural
		 * changes in the returned list are reflected in this list, and vice-versa.
		 * The returned list supports all of the optional list operations supported
		 * by this list.<p>
		 *
		 * This method eliminates the need for explicit range operations (of
		 * the sort that commonly exist for arrays).  Any operation that expects
		 * a list can be used as a range operation by passing a subList view
		 * instead of a whole list.  For example, the following idiom
		 * removes a range of elements from a list:
		 * <pre>{@code
		 *      list.subList(from, to).clear();
		 * }</pre>
		 * Similar idioms may be constructed for <tt>indexOf</tt> and
		 * <tt>lastIndexOf</tt>, and all of the algorithms in the
		 * <tt>Collections</tt> class can be applied to a subList.<p>
		 *
		 * The semantics of the list returned by this method become undefined if
		 * the backing list (i.e., this list) is <i>structurally modified</i> in
		 * any way other than via the returned list.  (Structural modifications are
		 * those that change the size of this list, or otherwise perturb it in such
		 * a fashion that iterations in progress may yield incorrect results.)
		 *
		 * @param fromIndex low endpoint (inclusive) of the subList
		 * @param toIndex high endpoint (exclusive) of the subList
		 * @return a view of the specified range within this list
		 * @throws IndexOutOfBoundsException for an illegal endpoint index value
		 *         (<tt>fromIndex &lt; 0 || toIndex &gt; size ||
		 *         fromIndex &gt; toIndex</tt>)
		 */
		<E> List<E> subList(String namespace,String key,int fromIndex, int toIndex,E e);


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
