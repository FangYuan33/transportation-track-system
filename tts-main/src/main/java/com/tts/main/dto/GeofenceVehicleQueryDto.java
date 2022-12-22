package com.tts.main.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class GeofenceVehicleQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 　租户id
     */
    private Long tenantId;

    /**
     * 　车辆id 列表
     */
    private List<Long> vehicleIdList;

    /**
     * 　geohash 长度位数
     */
    private Integer level;

    /**
     * 　geohash 列表
     **/
    private List<String> geohashList;

    /**
     * 查询起始时间
     */
    private LocalDateTime startTime;

}
