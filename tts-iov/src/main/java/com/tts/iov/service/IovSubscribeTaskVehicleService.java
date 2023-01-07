package com.tts.iov.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tts.iov.domain.IovSubscribeTaskVehicle;
import com.tts.remote.dto.IovSubscribeTaskVehicleDto;

/**
 * 车辆订阅任务明细服务层
 */
public interface IovSubscribeTaskVehicleService extends IService<IovSubscribeTaskVehicle> {

    /**
     * 添加车辆订阅任务
     */
    boolean addVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto);

    /**
     * 移除车辆订阅任务
     */
    boolean removeVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto);
}
