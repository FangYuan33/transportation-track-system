package com.tts.iov.facade;

import com.tts.remote.dto.CoordinatePointResultDto;
import com.tts.remote.dto.IovVehicleQueryDto;

import java.util.List;

/**
 * 通用的IOV信息查询通用接口，具体的实现类需要包含ivo设备类型名
 * 这样在使用策略模式的时候就能够轻易获取
 */
public interface IovFacade {

    /**
     * 通过iov平台查询指定车辆最新坐标信息，相关信息不存入数据库
     * 可以同时查多个车的位置
     */
    List<CoordinatePointResultDto> queryIovVehicleLastLocationDirectly(IovVehicleQueryDto vehicleQueryDto);

    /**
     * 直接通过iov平台查询指定车辆的路径信息，相关信息不存入数据库
     * 一次只能查询一个车的轨迹
     */
    List<CoordinatePointResultDto> queryIovVehicleTrackDirectly(IovVehicleQueryDto vehicleQueryDto);
}
