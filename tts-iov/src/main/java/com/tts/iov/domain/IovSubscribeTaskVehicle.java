package com.tts.iov.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 车辆订阅任务明细
 *
 * @author FangYuan
 * @since 2023-01-07 15:58:19
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class IovSubscribeTaskVehicle extends BaseEntity {

    public IovSubscribeTaskVehicle(Long taskId, String vehicleNo) {
        this.taskId = taskId;
        this.vehicleNo = vehicleNo;
    }

    /**
     * 绑定的订阅任务ID
     */
    private Long taskId;

    /**
     * 车牌号
     */
    private String vehicleNo;
}
