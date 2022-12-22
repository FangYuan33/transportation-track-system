package com.tts.main.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 每个车的最新的坐标点
 * 无论是否有装车单id
 * 如果需要分表的话,按照租户id分表
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
@Data
public class TtsCoordinateLatest extends SimpleBaseEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3250365560386230565L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流水表对应id
     */
    private Long flowRecordId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 　车辆id
     */
    private Long vehicleId;

    /**
     * 装车单id
     */
    private Long shipmentId;

    /**
     * 　经度
     */
    private double longitude;

    /**
     * 　维度
     */
    private double latitude;

    /**
     * 接收坐标时候的服务器时间
     */
    private LocalDateTime receiverServerTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 　geohash 编码
     */
    private String geohash1;
    private String geohash2;
    private String geohash3;
    private String geohash4;
    private String geohash5;
    private String geohash6;
    private String geohash7;
    private String geohash8;
    private String geohash9;
    private String geohash10;
    private String geohash11;
    private String geohash12;
}
