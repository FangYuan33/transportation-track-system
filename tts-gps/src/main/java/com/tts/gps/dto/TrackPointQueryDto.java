package com.tts.gps.dto;

import lombok.Data;

@Data
public class TrackPointQueryDto {
    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 速度
     */
    private String speed;

    /**
     * 方向
     */
    private String direction;

    /**
     * 海拔高度
     */
    private String altitude;

    /**
     * 定位时间
     */
    private String time;

    /**
     * 详细地址
     */
    private String address;
}
