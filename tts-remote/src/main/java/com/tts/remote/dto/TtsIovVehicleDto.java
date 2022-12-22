package com.tts.remote.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * iov 的  车辆信息
 *
 * @author FangYuan
 * @since 2022-12-22 15:49:53
 */
@Data
public class TtsIovVehicleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 承运商编号
     */
    private String carrierCode;
    /**
     * iov 类型, 目前支持 SINOIOV 和 G7 ,全大写
     */
    private String iovType;
    /**
     * 业务系统中的车辆id
     */
    private Long vehicleId;
    /**
     * 车牌号
     */
    private String vehicleNo;
    /**
     * 车牌号颜色(1:蓝色,2:黄色)
     */
    private Integer vehicleColorType;
    /**
     * 业务系统的 装车单id ,如果没有 则为 -1
     */
    private String shipmentId;
}