package com.tts.main.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 轨迹查询 dto
 *
 * @author FangYuan
 * @since 2022-12-22 20:06:20
 */
@Data
public class TrackQueryDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3250366560386230565L;

    /**
     * 需要查询的租户id
     */
    private Long tenantId;

    /**
     * 需要查询的车辆id
     */
    private Long vehicleId;

    /**
     * 需要查询的装车单id
     */
    private Long shipmentId;

    /**
     * 需要查询的起始时间
     */
    private Date startTime;

    /**
     * 需要查询的结束时间
     */
    private Date endTime;

    /**
     * 精确度判断的表示
     */
    private boolean accurateFlag;
}
