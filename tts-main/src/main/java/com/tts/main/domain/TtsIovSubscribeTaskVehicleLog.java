package com.tts.main.domain;


import com.tts.framework.common.domain.SimpleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


/**
 * iiov 订阅任务车辆日志表
 *
 * @author FangYuan
 * @since 2022-12-22 17:32:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TtsIovSubscribeTaskVehicleLog extends SimpleBaseEntity {
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
     * 装车单号,如果没有装车单则为-1,多个装车单用逗号分隔
     */
    private String shipmentId;
    /**
     * 车牌号颜色(1:蓝色,2:黄色)
     */
    private Integer vehicleColorType;
    /**
     * 动作: 0:删除 1:插入
     */
    private String operation;
    /**
     * 服务器ip
     */
    private String serverIp;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
