package com.tts.base.constant;

/**
 * iov 任务状态
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
public enum IovTaskStateEnum {
		//无状态,用于表示新建任务的preState ,实际中不会有任务真的是这个状态
		NONE(-1),
		//待分配
		ALLOCATING(0),
		//已分配
		ALLOCATED(1),
		//任务运行中
		RUNNING(2),
		//停止中
		STOPPING(3),
		//正常停止
		STOPPED(4);

		private Integer value;


		IovTaskStateEnum(int value) {
				this.value = value;
		}

		public Integer getValue() {
				return value;
		}
}
