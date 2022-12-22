package com.tts.iov.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iov 平台 标准 车辆信息采点信息(包括,坐标,地址,温度 等各种点位信息)
 * 所有跟车辆信息相关的入参参数都在里面
 *
 * @author FangYuan
 * @since 2022-12-22 16:19:46
 */
@Data
public class IovVehiclePointResultDto implements Serializable {

    private static final long serialVersionUID = 1L;
    //车牌号
    private String vehicleNo;
    //查询时间范围(单位:小时)
    private Integer timeNearBy;
    //查询起始时间
    private Date timeStart;
    //查询结束时间
    private Date timeEnd;

    //车辆纬度
    private String lat;
    //车辆经度
    private String lon;
    //车辆地理位置信息
    private String adr;
    //车辆速度
    private String spd;
    //车辆方向
    private String drc;
    //省
    private String province;
    //市
    private String city;
    //县
    private String country;
    //里程
    private String mil;
    //车辆定位时间 , 格式固定 为时间戳 long型那个
    private String utc;
    //车牌号
    private String vno;
    //车牌颜色
    private String vco;

    //海拔高度
    private String altitude;

    //温度1
    private String temperature1;
    //温度2
    private String temperature2;
    //温度3
    private String temperature3;
    //温度4
    private String temperature4;
    //湿度1
    private String humidity;
    //湿度2
    private String humidity2;
    //湿度3
    private String humidity3;
    //湿度4
    private String humidity4;

    //备注,用来描述车辆的状态等信息.目前只有易流有对应信息
    private String remark;

    //采集设备类型
    private String deviceType;

    //传感器采集时间,如果是多个传感器默认使用第一个温度传感器的时间
    private String sensorTime;

}
