package com.tts.framework.cache.redis.operator;


import java.util.concurrent.TimeUnit;

/**
 * redis lock 操作接口
 * 大家都知道，如果负责储存这个分布式锁的Redisson节点宕机以后，而且这个锁正好处于锁住的状态时，这个锁会出现锁死的状态。为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。
 *
 * 另外Redisson还通过加锁的方法提供了leaseTime的参数来指定加锁的时间。超过这个时间后锁便自动解开了。
 *
 * 。也就是说只有拥有锁的进程才能解锁，其他进程解锁则会抛出IllegalMonitorStateException错误
 *
 * @author FangYuan
 * @since 2021-01-21
 */
public interface RedisClientLockOperator {

		/**
		 * Returns name of object
		 *
		 * @return name - name of object
		 */
		String getName(String namespace,String key);

		/**
		 * Acquires the lock with defined <code>leaseTime</code>.
		 * Waits if necessary until lock became available.
		 *
		 * Lock will be released automatically after defined <code>leaseTime</code> interval.
		 *
		 * @param leaseTime the maximum time to hold the lock after it's acquisition,
		 *        if it hasn't already been released by invoking <code>unlock</code>.
		 *        If leaseTime is -1, hold the lock until explicitly unlocked.
		 * @param unit the time unit
		 * @throws InterruptedException - if the thread is interrupted
		 */
		Boolean lockInterruptibly(String namespace,String key,long leaseTime, TimeUnit unit) throws InterruptedException;

		/**
		 * Tries to acquire the lock with defined <code>leaseTime</code>.
		 * Waits up to defined <code>waitTime</code> if necessary until the lock became available.
		 *
		 * Lock will be released automatically after defined <code>leaseTime</code> interval.
		 *
		 * @param waitTime the maximum time to acquire the lock
		 * @param leaseTime lease time
		 * @param unit time unit
		 * @return <code>true</code> if lock is successfully acquired,
		 *          otherwise <code>false</code> if lock is already set.
		 * @throws InterruptedException - if the thread is interrupted
		 */
		Boolean tryLock(String namespace,String key,long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

		/**
		 * Acquires the lock with defined <code>leaseTime</code>.
		 * Waits if necessary until lock became available.
		 *
		 * Lock will be released automatically after defined <code>leaseTime</code> interval.
		 *
		 * @param leaseTime the maximum time to hold the lock after it's acquisition,
		 *        if it hasn't already been released by invoking <code>unlock</code>.
		 *        If leaseTime is -1, hold the lock until explicitly unlocked.
		 * @param unit the time unit
		 *
		 */
		Boolean lock(String namespace,String key,long leaseTime, TimeUnit unit);

		/**
		 * Unlocks the lock independently of its state
		 *
		 * @return <code>true</code> if lock existed and now unlocked
		 *          otherwise <code>false</code>
		 */
		Boolean forceUnlock(String namespace,String key);

		/**
		 * Checks if the lock locked by any thread
		 *
		 * @return <code>true</code> if locked otherwise <code>false</code>
		 */
		Boolean isLocked(String namespace,String key);

		/**
		 * Checks if the lock is held by thread with defined <code>threadId</code>
		 *
		 * @param threadId Thread ID of locking thread
		 * @return <code>true</code> if held by thread with given id
		 *          otherwise <code>false</code>
		 */
		Boolean isHeldByThread(String namespace,String key,long threadId);

		/**
		 * Checks if this lock is held by the current thread
		 *
		 * @return <code>true</code> if held by current thread
		 * otherwise <code>false</code>
		 */
		Boolean isHeldByCurrentThread(String namespace,String key);

		/**
		 * Number of holds on this lock by the current thread
		 *
		 * @return holds or <code>0</code> if this lock is not held by current thread
		 */
		Integer getHoldCount(String namespace,String key);

		/**
		 * Remaining time to live of the lock
		 *
		 * @return time in milliseconds
		 *          -2 if the lock does not exist.
		 *          -1 if the lock exists but has no associated expire.
		 */
		Long remainTimeToLive(String namespace,String key);


		/**
		 * Acquires the lock.
		 *
		 * <p>If the lock is not available then the current thread becomes
		 * disabled for thread scheduling purposes and lies dormant until the
		 * lock has been acquired.
		 *
		 * <p><b>Implementation Considerations</b>
		 *
		 * <p>A {@code Lock} implementation may be able to detect erroneous use
		 * of the lock, such as an invocation that would cause deadlock, and
		 * may throw an (unchecked) exception in such circumstances.  The
		 * circumstances and the exception type must be documented by that
		 * {@code Lock} implementation.
		 */
		Boolean lock(String namespace,String key);

		/**
		 * Acquires the lock unless the current thread is
		 * {@linkplain Thread#interrupt interrupted}.
		 *
		 * <p>Acquires the lock if it is available and returns immediately.
		 *
		 * <p>If the lock is not available then the current thread becomes
		 * disabled for thread scheduling purposes and lies dormant until
		 * one of two things happens:
		 *
		 * <ul>
		 * <li>The lock is acquired by the current thread; or
		 * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
		 * current thread, and interruption of lock acquisition is supported.
		 * </ul>
		 *
		 * <p>If the current thread:
		 * <ul>
		 * <li>has its interrupted status set on entry to this method; or
		 * <li>is {@linkplain Thread#interrupt interrupted} while acquiring the
		 * lock, and interruption of lock acquisition is supported,
		 * </ul>
		 * then {@link InterruptedException} is thrown and the current thread's
		 * interrupted status is cleared.
		 *
		 * <p><b>Implementation Considerations</b>
		 *
		 * <p>The ability to interrupt a lock acquisition in some
		 * implementations may not be possible, and if possible may be an
		 * expensive operation.  The programmer should be aware that this
		 * may be the case. An implementation should document when this is
		 * the case.
		 *
		 * <p>An implementation can favor responding to an interrupt over
		 * normal method return.
		 *
		 * <p>A {@code Lock} implementation may be able to detect
		 * erroneous use of the lock, such as an invocation that would
		 * cause deadlock, and may throw an (unchecked) exception in such
		 * circumstances.  The circumstances and the exception type must
		 * be documented by that {@code Lock} implementation.
		 *
		 * @throws InterruptedException if the current thread is
		 *         interrupted while acquiring the lock (and interruption
		 *         of lock acquisition is supported)
		 */
		Boolean lockInterruptibly(String namespace,String key) throws InterruptedException;

		/**
		 * Acquires the lock only if it is free at the time of invocation.
		 *
		 * <p>Acquires the lock if it is available and returns immediately
		 * with the value {@code true}.
		 * If the lock is not available then this method will return
		 * immediately with the value {@code false}.
		 *
		 * <p>A typical usage idiom for this method would be:
		 *  <pre> {@code
		 * Lock lock = ...;
		 * if (lock.tryLock()) {
		 *   try {
		 *     // manipulate protected state
		 *   } finally {
		 *     lock.unlock();
		 *   }
		 * } else {
		 *   // perform alternative actions
		 * }}</pre>
		 *
		 * This usage ensures that the lock is unlocked if it was acquired, and
		 * doesn't try to unlock if the lock was not acquired.
		 *
		 * @return {@code true} if the lock was acquired and
		 *         {@code false} otherwise
		 */
		Boolean tryLock(String namespace,String key);

		/**
		 * Acquires the lock if it is free within the given waiting time and the
		 * current thread has not been {@linkplain Thread#interrupt interrupted}.
		 *
		 * <p>If the lock is available this method returns immediately
		 * with the value {@code true}.
		 * If the lock is not available then
		 * the current thread becomes disabled for thread scheduling
		 * purposes and lies dormant until one of three things happens:
		 * <ul>
		 * <li>The lock is acquired by the current thread; or
		 * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
		 * current thread, and interruption of lock acquisition is supported; or
		 * <li>The specified waiting time elapses
		 * </ul>
		 *
		 * <p>If the lock is acquired then the value {@code true} is returned.
		 *
		 * <p>If the current thread:
		 * <ul>
		 * <li>has its interrupted status set on entry to this method; or
		 * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
		 * the lock, and interruption of lock acquisition is supported,
		 * </ul>
		 * then {@link InterruptedException} is thrown and the current thread's
		 * interrupted status is cleared.
		 *
		 * <p>If the specified waiting time elapses then the value {@code false}
		 * is returned.
		 * If the time is
		 * less than or equal to zero, the method will not wait at all.
		 *
		 * <p><b>Implementation Considerations</b>
		 *
		 * <p>The ability to interrupt a lock acquisition in some implementations
		 * may not be possible, and if possible may
		 * be an expensive operation.
		 * The programmer should be aware that this may be the case. An
		 * implementation should document when this is the case.
		 *
		 * <p>An implementation can favor responding to an interrupt over normal
		 * method return, or reporting a timeout.
		 *
		 * <p>A {@code Lock} implementation may be able to detect
		 * erroneous use of the lock, such as an invocation that would cause
		 * deadlock, and may throw an (unchecked) exception in such circumstances.
		 * The circumstances and the exception type must be documented by that
		 * {@code Lock} implementation.
		 *
		 * @param time the maximum time to wait for the lock
		 * @param unit the time unit of the {@code time} argument
		 * @return {@code true} if the lock was acquired and {@code false}
		 *         if the waiting time elapsed before the lock was acquired
		 *
		 * @throws InterruptedException if the current thread is interrupted
		 *         while acquiring the lock (and interruption of lock
		 *         acquisition is supported)
		 */
		Boolean tryLock(String namespace,String key,long time, TimeUnit unit) throws InterruptedException;

		/**
		 * Releases the lock.
		 *
		 * <p><b>Implementation Considerations</b>
		 *
		 * <p>A {@code Lock} implementation will usually impose
		 * restrictions on which thread can release a lock (typically only the
		 * holder of the lock can release it) and may throw
		 * an (unchecked) exception if the restriction is violated.
		 * Any restrictions and the exception
		 * type must be documented by that {@code Lock} implementation.
		 */
		Boolean unlock(String namespace,String key);


}
