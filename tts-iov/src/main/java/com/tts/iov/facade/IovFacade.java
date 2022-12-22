package com.tts.iov.facade;

import com.tts.iov.dto.IovVehicleInputDto;
import com.tts.iov.dto.IovVehiclePointResultDto;

import java.util.List;
import java.util.Properties;

/**
 * 坐标系 统一为  GCJ02
 */
public interface IovFacade {
    /**
     * 初始化属性
     */
    void initProperties(Properties properties) throws Exception;

    /**
     * 返回车辆的最新位置信息
     */
    List<IovVehiclePointResultDto> queryVehicleLastLocation(IovVehicleInputDto iovVehicleInputDto) throws Exception;

    /**
     * 根据 车牌号 ,查询起始时间 与 查询截止时间 查询 轨迹信息
     * 注意  查询时间跨度不能超过24小时
     */
    List<IovVehiclePointResultDto> queryVehicleTrack(IovVehicleInputDto iovVehicleInputDto) throws Exception;

}
