package com.tts.iov.dto;


import com.tts.remote.constant.CoordinateTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * iov 平台 标准车辆信息入参dto
 * 所有跟车辆信息相关的入参参数都在里面
 *
 * @author FangYuan
 * @since 2022-12-22 16:16:57
 */
@Data
public class IovVehicleInputDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //车牌号
    private List<String> vehicleNoList;
    //车牌颜色
    private List<Integer> vehicleNoColorList;
    //查询时间范围(单位:小时)
    private Integer timeNearBy;
    //查询起始时间
    private LocalDateTime timeStart;
    //查询结束时间
    private LocalDateTime timeEnd;

    //查询时间间隔,单位为秒,例如数据间隔10秒, 或者数据间隔 20秒
    private Integer timeInterval;

    //坐标系,如果为空 则默认为火星坐标系,仅用于 直接查询的  remote 接口
    private CoordinateTypeEnum coordinateTypeEnum;

    //是否返回车辆位置的路名信息, 1 为需要, 0 为不需要
    private Integer isPlaceName;

}
