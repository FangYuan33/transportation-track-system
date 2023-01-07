package com.tts.iov.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车辆订阅任务明细
 *
 * @author FangYuan
 * @since 2023-01-07 15:58:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IovSubscribeTaskVehicle extends BaseEntity {

    /**
     * 绑定的订阅任务ID
     */
    private Long taskId;

    /**
     * 车牌号
     */
    private String vehicleNo;
}
