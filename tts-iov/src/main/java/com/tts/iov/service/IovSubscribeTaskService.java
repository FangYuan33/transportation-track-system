package com.tts.iov.service;

import com.tts.iov.domain.IovSubscribeTask;

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
}
