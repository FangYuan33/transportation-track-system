package com.tts.main.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 每个车的运算后的轨迹信息
 * 只运算有装车单的坐标点
 * 按照车辆id分表
 *
 * @author FangYuan
 * @since 2021-01-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsCoordinateCal extends SimpleBaseEntity {

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
    private Date receiverServerTime;

    /**
     * 当前这段距离里程
     */
    private double currentDistance = 0;

    /**
     * 整个装车单累计里程
     */
    private double wholeDistance = 0;

    /**
     * 当前这段耗时
     */
    private long currentDuration = 0;

    /**
     * 整个装车单累计耗时
     */
    private long wholeDuration = 0;

    /**
     * 当前这段距离车速
     */
    private double currentSpeed = 0;

    /**
     * 整个装车单累计车速
     */
    private double wholeSpeed = 0;

    /**
     * 　运算时间
     */
    private Date calTime;

    /**
     * 是否参与运算结果
     */
    private boolean isCal;

}
