package com.tts.iov.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tts.iov.domain.IovSubscribeTaskVehicle;
import com.tts.remote.dto.IovSubscribeTaskVehicleDto;

import java.util.List;

/**
 * 车辆订阅任务明细服务层
 */
public interface IovSubscribeTaskVehicleService extends IService<IovSubscribeTaskVehicle> {

    /**
     * 添加车辆订阅任务
     */
    boolean saveOrUpdateVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto);

    /**
     * 移除车辆订阅任务
     */
    boolean removeVehicleTask(IovSubscribeTaskVehicleDto taskVehicleDto);

    /**
     * 根据订阅任务获取该任务下所有要执行点位拉取的车辆任务
     */
    List<IovSubscribeTaskVehicle> listInTaskIdList(List<Long> taskIdList);

    /**
     * 根据ID获取该车任务的iov设备类型
     */
    String getIovTypeById(Long id);

    /**
     * 根据实体信息更新数据库数据
     */
    void updateByEntity(IovSubscribeTaskVehicle vehicleTask);
}
