package com.tts.remote.service;

import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.IovConfigDto;
import com.tts.remote.dto.IovSubscribeTaskVehicleDto;
import com.tts.remote.dto.IovVehicleQueryDto;

import java.util.List;

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

    /**
     * 通过iov平台查询指定车辆最新坐标信息，相关信息不存入数据库
     * 一次只能查询一个车的位置
     */
    List<CoordinatePointResultDto> queryIovVehicleLastLocationDirectly(IovVehicleQueryDto vehicleQueryDto);

    /**
     * 直接通过iov平台查询指定车辆的路径信息，相关信息不存入数据库
     * 一次只能查询一个车的轨迹
     */
    List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(IovVehicleQueryDto vehicleQueryDto);
}
