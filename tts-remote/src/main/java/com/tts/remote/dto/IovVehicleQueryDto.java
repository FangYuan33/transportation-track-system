package com.tts.remote.dto;

import com.tts.remote.enums.IovTypeEnums;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车辆信息相关的查询入参
 *
 * @author FangYuan
 * @since 2023-01-08 15:33:21
 */
public class IovVehicleQueryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String carrierCode;

    private IovTypeEnums iovTypeEnum;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 查询起始时间
     */
    private LocalDateTime timeStart;

    /**
     * 查询结束时间
     */
    private LocalDateTime timeEnd;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public IovTypeEnums getIovTypeEnum() {
        return iovTypeEnum;
    }

    public void setIovTypeEnum(IovTypeEnums iovTypeEnum) {
        this.iovTypeEnum = iovTypeEnum;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }
}
