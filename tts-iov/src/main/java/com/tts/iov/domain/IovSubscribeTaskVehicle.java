package com.tts.iov.domain;

import com.tts.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    public IovSubscribeTaskVehicle(Long taskId, String vehicleNo, LocalDateTime startTime) {
        this.taskId = taskId;
        this.vehicleNo = vehicleNo;
        this.startTime = startTime;
    }

    /**
     * 绑定的订阅任务ID
     */
    private Long taskId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 点位拉取任务开始时间
     */
    private LocalDateTime startTime;
}
