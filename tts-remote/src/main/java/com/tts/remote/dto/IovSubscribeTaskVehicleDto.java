package com.tts.remote.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class IovSubscribeTaskVehicleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String carrierCode;

    private String iovType;

    private String vehicleNo;

    private LocalDateTime startTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getIovType() {
        return iovType;
    }

    public void setIovType(String iovType) {
        this.iovType = iovType;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
