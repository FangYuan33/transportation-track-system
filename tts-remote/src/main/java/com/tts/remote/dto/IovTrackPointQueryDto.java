package com.tts.remote.dto;

import java.io.Serializable;

public class IovTrackPointQueryDto implements Serializable {
    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 方向
     */
    private String direction;

    /**
     * 定位时间
     */
    private String time;

    /**
     * 详细地址
     */
    private String address;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
