package com.tts.remote.dto;

public class IovSubscribeTaskVehicleDto {

    private String carrierCode;

    private String iovType;

    private String vehicleNo;

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
