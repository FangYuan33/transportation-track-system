package com.tts.remote.service;

import com.tts.remote.dto.IovConfigDto;
import com.tts.remote.dto.IovSubscribeTaskVehicleDto;

public interface SystemRemoteService {

    /**
     * 新增or修改Iov Config
     */
    boolean saveOrUpdateIovConfig(IovConfigDto iovConfig);

    /**
     * 开启订阅任务，如果对应任务已经存在，则重新启动
     */
    boolean startSubscribeTask(String carrierCode, String iovType);

    /**
     * 关闭该承运商的订阅任务
     */
    boolean stopSubscribeTask(String carrierCode, String iovType);

    /**
     * 添加车辆订阅任务
     */
    boolean addVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto);

    /**
     * 移除车辆订阅任务
     */
    boolean removeVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto);
}
