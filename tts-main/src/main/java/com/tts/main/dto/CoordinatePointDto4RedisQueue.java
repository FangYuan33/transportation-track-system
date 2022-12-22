package com.tts.main.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用来缓存在redis的 队列 中 的坐标信息 dto
 */
@Data
public class CoordinatePointDto4RedisQueue implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库中对应记录的主键id
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;


    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 装车单id
     */
    private Long shipmentId;

    /**
     * 定位地图坐标系类别
     */
    private String mapCoordinateSystem;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 接收到坐标时的服务器当前时间
     */
    private Date receiverServerTime;

    /**
     * 　geohash 编码
     */
    private String geoHash;

    /**
     * wkt 编码
     */
    private String pointWKT;

}
