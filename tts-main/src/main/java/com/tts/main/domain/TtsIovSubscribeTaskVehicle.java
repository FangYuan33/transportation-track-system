package com.tts.main.domain;

import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


/**
 * iov 订阅 任务 车辆信息表
 * 只 插入,当停止订阅,或者有变化的时候就删除
 *
 * @author FangYuan
 * @since 2022-12-22 15:53:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsIovSubscribeTaskVehicle extends SimpleBaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * iov订阅任务id
     */
    private Long taskId;
    /**
     * 业务系统中的车辆ID
     */
    private Long vehicleId;
    /**
     * 车牌号
     */
    private String vehicleNo;
    /**
     * 装车单号,如果没有装车单则为-1 ,多个装车单用逗号分隔
     */
    private String shipmentId;
    /**
     * 车牌号颜色(1:蓝色,2:黄色)
     */
    private Integer vehicleColorType;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
