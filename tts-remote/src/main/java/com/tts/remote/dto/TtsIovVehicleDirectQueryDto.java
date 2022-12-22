package com.tts.remote.dto;

import com.tts.remote.constant.CoordinateTypeEnum;
import com.tts.remote.constant.TtsIovTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 基于  iov 平台 的 车辆
 * 所有跟车辆信息相关的入参参数都在里面
 *
 * @author FangYuan
 * @since 2022-12-22 16:02:06
 */
@Data
public class TtsIovVehicleDirectQueryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String carrierCode;
    private TtsIovTypeEnum iovTypeEnum;
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
    //是否记录日志,默认不记录
    private boolean isLog;
    //查询时间间隔,单位为秒,例如数据间隔10秒, 或者数据间隔 20秒
    private Integer timeInterval;
    //坐标系,如果为空 则默认为火星坐标系
    private CoordinateTypeEnum coordinateTypeEnum;

    //是否返回车辆位置的路名信息, 1 为需要, 0 为不需要
    private Integer isPlaceName;

}
