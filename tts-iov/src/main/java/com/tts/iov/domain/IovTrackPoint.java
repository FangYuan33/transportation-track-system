package com.tts.iov.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轨迹点位实体对象
 *
 * @author FangYuan
 * @since 2023-01-09 17:34:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IovTrackPoint extends BaseEntity {
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
