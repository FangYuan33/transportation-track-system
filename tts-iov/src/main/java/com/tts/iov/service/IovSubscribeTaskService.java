package com.tts.iov.service;

import com.tts.iov.domain.IovSubscribeTask;

import java.util.List;

/**
 * iov 任务订阅服务层
 */
public interface IovSubscribeTaskService {

    /**
     * 开启订阅任务，如果对应任务已经存在，则重新启动
     */
    boolean startSubscribeTask(String carrierCode, String iovType);

    /**
     * 关闭该承运商的订阅任务
     */
    boolean stopSubscribeTask(String carrierCode, String iovType);

    IovSubscribeTask selectByIovConfigIdAndCarrierCode(Long iovConfigId, String carrierCode);

    /**
     * 获取所有正在运行的任务
     */
    List<IovSubscribeTask> listRunningTask();

    /**
     * 获取所有服务下的任务
     */
    List<IovSubscribeTask> listByServerNames(List<String> serverNames);

    /**
     * 将任务置为待分配
     */
    void allocatingTask(IovSubscribeTask task);

    /**
     * 获取所有待分配的任务
     */
    List<IovSubscribeTask> listAllocatingTask();

    /**
     * 分配任务给某个服务
     *
     * @param serverName 某个服务的名字
     */
    void allocatedTask(IovSubscribeTask task, String serverName);

    /**
     * 获取分配给当前节点的任务
     */
    List<IovSubscribeTask> listCurrentNodeAllocatedTask();

    /**
     * 启动已分配的任务
     */
    void runningTask(IovSubscribeTask allocatedTask);
}
