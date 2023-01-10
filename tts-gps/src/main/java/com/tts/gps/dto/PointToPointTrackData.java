package com.tts.gps.dto;

import lombok.Data;

/**
 * 端对端接口对接 车辆轨迹结果对象
 *
 *
 * @author FangYuan
 * @since 2022-11-24 16:13:37
 */
@Data
public class PointToPointTrackData {

    /**
     * gps时间 格式：yyyyMMdd/HHmmss
     */
    private String time;

    /**
     * 经度
     */
    private String lng;

    /**
     * 定位纬度
     */
    private String lat;

    /**
     * 坐标系 GCJ02/WGS84/BD09/UNKNOW
     */
    private String coordinate;

    /**
     * GPS速度 km/h
     */
    private String speed;

    /**
     * 方向 与正北方向夹角
     */
    private String course;

    /**
     * 与上一点距离 单位厘米
     */
    private String distance;
}
