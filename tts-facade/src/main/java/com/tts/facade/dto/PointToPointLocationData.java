package com.tts.facade.dto;

import lombok.Data;

/**
 * 端对端接口对接 车辆位置结果对象
 *
 * @author FangYuan
 * @since 2022-11-24 15:26:29
 */
@Data
public class PointToPointLocationData {

    /**
     * 运单号
     */
    private String wayBillNum;

    /**
     * 定位时间 时间戳
     */
    private String gpstime;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 坐标系 GCJ02/WGS84/BD09/UNKNOW
     */
    private String coordinate;

    /**
     * 速度 km/h
     */
    private String speed;

    /**
     * 方向 与正北方向夹角
     */
    private String course;
}
