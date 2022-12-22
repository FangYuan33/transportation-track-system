package com.tts.framework.cache.redis.operator;


import org.redisson.api.RScheduledExecutorService;

/*
*  Redisson的分布式调度任务服务实现了java.util.concurrent.ScheduledExecutorService接口，
* 支持在不同的独立节点里执行基于java.util.concurrent.Callable接口或java.lang.Runnable接口的任务。
* Redisson独立节点按顺序运行Redis列队里的任务。调度任务是一种需要在未来某个指定时间运行一次或多次的特殊任务。
* @author FangYuan
* @since 2021-01-21
*/
public interface RedisClientScheduledExecutorOperator {

		/**
		 * Returns ScheduledExecutorService by name
		 *
		 * @param name - name of object
		 * @return ScheduledExecutorService object
		 */
		RScheduledExecutorService getExecutorService(String namespace,String key);
}
