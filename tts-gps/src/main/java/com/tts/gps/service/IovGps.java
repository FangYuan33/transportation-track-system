package com.tts.gps.service;

import com.tts.gps.dto.GpsCoordinatePointResultDto;
import com.tts.gps.dto.GpsVehicleQueryDto;

import java.util.List;
import java.util.Map;

/**
 * 通用的IOV信息查询通用接口，具体的实现类需要指定ivo设备类型名
 * 这样在使用策略模式的时候就能够轻易获取
 */
public interface IovGps {

    /**
     * 初始化各个GPS设备的配置信息
     */
    void initialConfigInfo(Map<String, String> configInfo);

    /**
     * 通过iov平台查询指定车辆最新坐标信息，相关信息不存入数据库
     * 可以同时查多个车的位置
     */
    List<GpsCoordinatePointResultDto> queryIovVehicleLastLocationDirectly(GpsVehicleQueryDto vehicleQueryDto);

    /**
     * 直接通过iov平台查询指定车辆的路径信息，相关信息不存入数据库
     * 一次只能查询一个车的轨迹
     */
    List<GpsCoordinatePointResultDto> queryIovVehicleTrackDirectly(GpsVehicleQueryDto vehicleQueryDto);
}
